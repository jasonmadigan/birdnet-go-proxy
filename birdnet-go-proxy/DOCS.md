This addon creates a proxy to a Birdnet-go server run separately from Home Assistant so that you can have the benefit of access in the sidebar without running Birdnet-go as an addon.

Note that this addon does not run Birdnet-go itself.

## Configuration

### Option: `server`

The `server` option sets the address of the Birdnet-go server.

This must be in the format `http[s]://host:port`. The following are valid examples:

- `http://birdnet.local:8080`
- `http://192.168.0.101:8080`

### Option: `proxy_pass_host`

Determines whether we should pass the host we're running on (for example,
`homeassistant.local`) to the server we're proxying to. In general, you probably
want this to be set to `true`.

Set to `false` if the server needs to receive the host of the birdnet-go instance
(not the host Home Assistant or this addon are running on). This might be the case
if your birdnet-go instance is behind an SSL proxy (like Traefik or Caddy), which
needs to receive the birdnet-go host in order to route the request correctly.

### Option: `proxy_pass_real_ip`

Determines whether we should pass the client's real IP address to the server we're proxying to. In general, you probably
want this to be set to `true`.

Set to `false` if you need to know the request is coming from the HA IP. This might be the case if your birdnet-go instance is behind a proxy which only allows specific IPs to connect.

## Required Dependencies

- Network access to running Birdnet-go server
