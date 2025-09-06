A simple TCP server that sends back whatever the client sends.

This contains 4 classes:
1. EchoClient: It initiates a message when the connection is open and echo's back any received data to the server.
2. EchoServer: Echo's back any data that is received from the client.
3. EchoClientHandler: Handles the client connection and communication.
4. EchoServerHandler: Handles the server connection and communication.


Observations:

Here is the mapping of components and their relationship with threads in the Netty EchoServer architecture:
1. EventLoopGroup: A group of EventLoop threads responsible for handling all I/O events for Channels. Each EventLoop is backed by a single thread that handles multiple Channels. It manages threading and concurrency.
2. ServerBootstrap: The bootstrap helper class that sets up the server configuration, including setting the EventLoopGroup, Channel type, options, and initializing pipeline handlers. It acts as a factory linking EventLoopGroups to Channels.
3. Channel: Represents a connection or a component that performs I/O operations (e.g., server-side channel listening for connections). Channels are registered and assigned to an EventLoop (thread) for processing events.
4. ChannelInitializer: A helper class used during Channel creation to configure the Channel pipeline with handlers. It runs once per Channel to set up handlers like the EchoServerHandler.
5. EchoServerHandler: The user-defined ChannelHandler that processes the business logic, here echoing received messages. It is invoked by the EventLoop thread for its Channel to handle inbound and outbound data.
6. Thread: Each EventLoop is a single thread that handles the I/O and events of one or more Channels. This threading model allows efficient non-blocking I/O and concurrency management without creating one thread per connection.

An EventLoop (backed by a single thread) manages multiple channels concurrently. It uses Java NIO selectors to multiplex I/O events for all these Channels on that single thread. This design allows handling thousands of connections efficiently without a one-thread-per-channel overhead.
1. One NioServerSocketChannel listens for incoming connection requests.
2. Each accepted connection creates a new Channel instance (e.g., NioSocketChannel).
3. Multiple such client Channels are assigned to an EventLoop thread, which handles their I/O asynchronously.
4. The EventLoop thread uses NIO selectors to detect I/O readiness on these Channels and dispatches events in an event-driven manner.
5. This multiplexing of many Channels on few threads is a core feature enabling Netty's high scalability and performance.