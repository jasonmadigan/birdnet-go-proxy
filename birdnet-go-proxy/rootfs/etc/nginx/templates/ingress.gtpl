server {
    listen 4003 default_server;

    include /etc/nginx/includes/server_params.conf;

    location / {
        allow   172.30.32.2;
        deny    all;

        # Enable sub_filter to rewrite URLs in the response body
        sub_filter_once off;  # Apply sub_filter to all matching instances

        # Broad sub_filter replacements for common paths
        sub_filter 'href="/' 'href="{{ .entry }}/';
        sub_filter 'src="/' 'src="{{ .entry }}/';
        sub_filter '="/' '="{{ .entry }}/';  # Generic double-quote match

        # Specific sub_filter for JavaScript-generated or dynamic paths
        sub_filter '"/settings/save' '"{{ .entry }}/settings/save';

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
