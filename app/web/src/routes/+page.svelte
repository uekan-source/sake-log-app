<script lang="ts">
  import { onMount } from 'svelte';
  import { ping } from '$lib/system';
  import { fetchMe, signup, login, logout, type User } from '$lib/user';

  let health = $state('...');
  let me = $state<User | null>(null);
  let error = $state('');

  let email = $state('');
  let password = $state('');
  let name = $state('');

  async function refresh() {
    me = await fetchMe();
  }

  async function onSignup(event: SubmitEvent) {
    event.preventDefault();
    error = '';
    const result = await signup(email, password, name);
    if (!result.ok) return (error = result.error);
    await refresh();
  }

  async function onLogin(event: SubmitEvent) {
    event.preventDefault();
    error = '';
    const result = await login(email, password);
    if (!result.ok) return (error = result.error);
    await refresh();
  }

  async function onLogout() {
    error = '';
    const result = await logout();
    if (!result.ok) return (error = result.error);
    await refresh();
  }

  onMount(async () => {
    health = await ping();
    await refresh();
  });
</script>

<h1>education-book-scala-app</h1>
<p>API health (<code>/ping</code>): <strong>{health}</strong></p>

{#if error}<p style="color: crimson;">{error}</p>{/if}

{#if me}
  <p>ログイン中: <strong>{me.name}</strong>（{me.email}）</p>
  <button onclick={onLogout}>ログアウト</button>
{:else}
  <h2>新規登録</h2>
  <form onsubmit={onSignup}>
    <input placeholder="name" bind:value={name} />
    <input placeholder="email" type="email" bind:value={email} />
    <input placeholder="password (8文字以上)" type="password" bind:value={password} />
    <button type="submit">登録</button>
  </form>

  <h2>ログイン</h2>
  <form onsubmit={onLogin}>
    <input placeholder="email" type="email" bind:value={email} />
    <input placeholder="password" type="password" bind:value={password} />
    <button type="submit">ログイン</button>
  </form>
{/if}
