Archived, just use https://github.com/lovelylain/hass_ingress/ and a config similar to:

```yaml
ingress:
  birdnetgo:
    ui_mode: toolbar
    title: Birdnet-go
    icon: mdi:bird
    url: http://192.168.1.230:8080
    rewrite:
      - mode: body
        match: '(href|src|=")\/(?!api/ingress/birdnetgo)'
        replace: '\1$http_x_ingress_path/'
      - mode: body
        match: '"\/(?!api/ingress/birdnetgo)'
        replace: '"$http_x_ingress_path/'
      - mode: body
        match: "'\/(?!api/ingress/birdnetgo)'"
        replace: "'$http_x_ingress_path/"
      - mode: body
        match: '"/settings/save'
        replace: '"$http_x_ingress_path/settings/save'
      - mode: body
        match: '"/audio-level'
        replace: '"$http_x_ingress_path/audio-level'
      - mode: body
        match: "'/audio-level'"
        replace: "'$http_x_ingress_path/audio-level'"
      - mode: body
        match: '"/(api|static)/(?!ingress/birdnetgo)'
        replace: '"$http_x_ingress_path/\1/'
      - mode: body
        match: "'/(api|static)/(?!ingress/birdnetgo)'"
        replace: "'$http_x_ingress_path/\\1/'"
      - mode: header
        name: "(Location|Set-Cookie)"
        match: "/api/(?!ingress/birdnetgo)"
        replace: "$http_x_ingress_path/api/"
      - mode: header
        name: "(Location|Set-Cookie)"
        match: "/static/(?!ingress/birdnetgo)"
        replace: "$http_x_ingress_path/static/"
    disable_stream: true
```
