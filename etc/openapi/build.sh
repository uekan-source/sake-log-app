#!/usr/bin/env bash
#
# Copyright IxiaS, Inc. All Rights Reserved.
#
# For the full copyright and license information,
# please view the LICENSE file that was distributed with this source code.
#
# Bundle the modular OpenAPI spec and generate the TypeScript types every
# frontend app consumes through `@app/api`.
#
# Usage:
#   ./etc/openapi/build.sh
#

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"

# Step 1: Resolve every $ref into one self-contained file.
echo "Bundling OpenAPI spec..."
npx --yes @redocly/cli@1 bundle "$SCRIPT_DIR/openapi.yaml" -o "$SCRIPT_DIR/.openapi.yaml"

# Step 2: Generate the TypeScript schema published as @app/api.
echo "Generating TypeScript types for @app/api..."
npx --yes openapi-typescript@7 "$SCRIPT_DIR/.openapi.yaml" \
  -o "$ROOT_DIR/app/packages/src/api/schema.d.ts"

echo "Done."
