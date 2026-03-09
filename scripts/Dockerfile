# Use a multi-stage build to get bombardier binary
FROM golang:1.23-alpine AS builder

RUN go install github.com/codesenberg/bombardier@latest

# Final image
FROM alpine:3.20

# Install runtime dependencies
RUN apk add --no-cache bash curl jq

# Copy bombardier from builder
COPY --from=builder /go/bin/bombardier /usr/local/bin/bombardier

# Set working directory
WORKDIR /app

# Copy scripts and body.json
COPY bombardier-runner.sh bombardier-get.sh bombardier-post.sh body.json ./

# Make scripts executable
RUN chmod +x *.sh

# Default environment variables
ENV TARGET_URL=http://localhost:8080

# Entrypoint
ENTRYPOINT ["./bombardier-runner.sh"]
