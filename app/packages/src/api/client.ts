/*
 * Copyright IxiaS, Inc. All Rights Reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */

import createClient from 'openapi-fetch';
import type { paths } from './schema';

/**
 * Typed API client shared by every frontend app.
 *
 * `paths` comes from `schema.d.ts`, generated out of `etc/openapi` by
 * `./etc/openapi/build.sh`, so `api.GET('/user/api/me')` knows its own
 * response shape and an unknown path is a compile error.
 *
 * Requests are same-origin: in development Vite proxies them to the Play
 * backend on :9000 (see each app's vite.config.ts). Authentication is the
 * httpOnly `session` cookie, which the browser attaches on its own — there is
 * no token to thread through here.
 */
export const api = createClient<paths>({
  baseUrl: typeof window !== 'undefined' ? window.location.origin : '',
});

export type { paths };

/** Shown when the request never reaches the server (offline / DNS / CORS). */
export const NETWORK_ERROR_MESSAGE = 'サーバーに接続できません';

/**
 * Resolve the end-user message for a failed API call.
 *
 * This backend speaks two different error dialects, and both land here:
 *
 *  - Controllers return 4xx as **text/plain** (`BadRequest("email is
 *    required")`), so `error` arrives as a plain string.
 *  - `mvc.ErrorHandler` returns the **JSON envelope** `{error, message, code,
 *    errors}` for anything it catches (route miss, 500).
 *
 * The status ladder is only a fallback for responses with no usable body, and
 * `fallback` is the per-call default for everything else.
 */
export function apiErrorMessage(
  error:    unknown,
  status:   number,
  fallback: string = '操作に失敗しました',
): string {
  if (typeof error === 'string' && error.length > 0) return error;
  if (error && typeof error === 'object') {
    const message = (error as { message?: unknown }).message;
    if (typeof message === 'string' && message.length > 0) return message;
  }
  if (status === 401) return '認証の有効期限が切れています。再ログインしてください';
  return fallback;
}

/** Shape openapi-fetch returns from any `api.GET/POST/PUT/DELETE` call. */
type ApiCall<D> = Promise<{ data?: D; error?: unknown; response: Response }>;

/**
 * Run an API call that returns a body on success, collapsing every failure
 * mode into one localized message: HTTP errors → the backend text/message
 * (or `fallback`), a thrown network error → [[NETWORK_ERROR_MESSAGE]].
 */
export async function request<D>(
  call:      ApiCall<D>,
  fallback?: string,
): Promise<{ ok: true; data: D } | { ok: false; error: string }> {
  try {
    const { data, error, response } = await call;
    if (data !== undefined) return { ok: true, data };
    return { ok: false, error: apiErrorMessage(error, response.status, fallback) };
  } catch {
    return { ok: false, error: NETWORK_ERROR_MESSAGE };
  }
}

/**
 * Like [[request]] but for calls whose success carries no body — a 204, or an
 * endpoint judged purely on status. Success is `response.ok`.
 */
export async function requestVoid(
  call:      ApiCall<unknown>,
  fallback?: string,
): Promise<{ ok: true } | { ok: false; error: string }> {
  try {
    const { error, response } = await call;
    if (response.ok) return { ok: true };
    return { ok: false, error: apiErrorMessage(error, response.status, fallback) };
  } catch {
    return { ok: false, error: NETWORK_ERROR_MESSAGE };
  }
}
