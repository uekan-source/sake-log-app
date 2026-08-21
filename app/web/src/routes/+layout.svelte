<script lang="ts">
  let { children } = $props();
</script>

<main style="max-width: 640px; margin: 2rem auto; font-family: sans-serif;">
  {@render children()}
</main>
