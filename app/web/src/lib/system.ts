/*
 * Copyright IxiaS, Inc. All Rights Reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */

import { api } from '@app/api';

/**
 * Backend health check. Returns the raw body (`ok`), or `NG` when the request
 * fails or never reaches the server — the caller only renders it.
 */
export async function ping(): Promise<string> {
  try {
    const { data, response } = await api.GET('/ping', { parseAs: 'text' });
    return response.ok && data ? data : 'NG';
  } catch {
    return 'NG';
  }
}
