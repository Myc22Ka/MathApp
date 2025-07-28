#!/bin/bash

# Function displays help options
show_help() {
    echo "Usage: ./scripts/update.sh [options]"
    echo ""
    echo "Options:"
    echo "  -l, --logs    Show logs for debugging"
    echo "  -h, --help    Show help"
    exit 0
}

# Checking for -h parameter
if [[ "$1" == "-h" || "$1" == "--help" ]]; then
    show_help
fi

# Checking if git is available
if ! command -v git &> /dev/null; then
    echo "❌ Error: git is not installed!"
    exit 1
fi

# Checking if docker-compose is available
if ! command -v docker-compose &> /dev/null; then
    echo "❌ Error: docker-compose is not installed!"
    exit 1
fi

echo "🚀 Rebuilding and starting the services..."
docker-compose up -d --build

# Checking for -l parameter
if [[ "$1" != "-l" && "$1" != "--logs" ]]; then
    echo "✅ Update complete! You are good to go."
    exit 0
fi

# Showing logs
docker-compose logs -f