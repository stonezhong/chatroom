timeline

1. a websocket upgrade request comes in
2. ChatRoomFilter will put client ip and port in sessions, key CLIENT_ENDPOINT_KEY
3. ChatEndpointConfigurator.modifyHandshake is called, it get the client ip and port from http session, and put it into user property, this property will be copied to websocket session
4. ChatRoomEndpoint.onOpen is called, it extract the client ip and port from websocket session