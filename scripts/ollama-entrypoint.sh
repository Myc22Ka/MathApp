#!/bin/sh

# Start server in background
ollama serve &

# Wait for server to be ready
sleep 10

# Pull model
ollama pull qwen2-math

# Kill initial server
pkill ollama

# Start server again in foreground
exec ollama serve
