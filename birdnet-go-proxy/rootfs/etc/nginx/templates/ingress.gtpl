server {
    listen 4003 default_server;

    include /etc/nginx/includes/server_params.conf;

    location / {
        allow   172.30.32.2;
        deny    all;

        # Enable sub_filter to rewrite URLs in the response body
        sub_filter_once off;  # Apply sub_filter to all matching instances

        # Rewriting asset paths
        sub_filter '/assets/' '{{ .entry }}/assets/';  # Replace asset URLs

        # Rewriting dynamic paths for HTML and JavaScript content
        sub_filter 'href="/top-birds"' 'href="{{ .entry }}/top-birds"';
        sub_filter 'href="/detections/recent"' 'href="{{ .entry }}/detections/recent"';
        sub_filter 'src="/top-birds"' 'src="{{ .entry }}/top-birds"';
        sub_filter 'src="/detections/recent"' 'src="{{ .entry }}/detections/recent"';

        # Rewriting media URLs
        sub_filter '"/media/spectrogram' '"{{ .entry }}/media/spectrogram';

        # Additional sub_filter rules for JavaScript requests
        sub_filter '"/top-birds' '"{{ .entry }}/top-birds';
        sub_filter '"/detections/recent' '"{{ .entry }}/detections/recent';

        # Rewriting settings and stats paths
        sub_filter 'href="/settings/' 'href="{{ .entry }}/settings/';
        sub_filter 'src="/settings/' 'src="{{ .entry }}/settings/';
        sub_filter '"/settings/' '"{{ .entry }}/settings/';
        sub_filter 'href="/stats"' 'href="{{ .entry }}/stats"';
        sub_filter 'src="/stats"' 'src="{{ .entry }}/stats"';
        sub_filter '"/stats' '"{{ .entry }}/stats';

        # Rewriting the root path '/' to include the ingress path
        sub_filter 'href="/"' 'href="{{ .entry }}/"';
        sub_filter 'src="/"' 'src="{{ .entry }}/"';
        sub_filter '"/"' '"{{ .entry }}/"';

        # Ensure the Content-Type is text/html or application/javascript for sub_filter to work
        proxy_set_header Accept-Encoding "";  # Disable gzip for sub_filter to work

        proxy_pass {{ .server }};
        proxy_set_header X-Ingress-Path {{ .entry }};

        {{ if .proxy_pass_host }}
          proxy_set_header Host $http_host;
        {{ end }}
        {{ if .proxy_pass_real_ip }}
          proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
          proxy_set_header X-Real-IP $remote_addr;
        {{ end }}

        include /etc/nginx/includes/proxy_params.conf;
    }

    # Serve assets without rewriting MIME types
    location ~* \.(css|js)$ {
        proxy_pass {{ .server }};
        proxy_set_header X-Ingress-Path {{ .entry }};
    }
}
