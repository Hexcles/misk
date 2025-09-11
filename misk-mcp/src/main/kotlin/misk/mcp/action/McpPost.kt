package misk.mcp.action

import misk.annotation.ExperimentalMiskApi
import misk.web.Post
import misk.web.RequestContentType
import misk.web.ResponseContentType
import misk.web.mediatype.MediaTypes

/**
 * Marks a web action method as an MCP (Model Context Protocol) HTTP POST endpoint for client-to-server messaging.
 *
 * This annotation creates endpoints that handle client-to-server JSON-RPC 2.0 messages as specified
 * in the MCP transport specification for "sending messages to the server". The endpoint accepts
 * JSON-RPC requests from MCP clients and responds with Server-Sent Events (SSE) for real-time
 * bidirectional communication.
 *
 * ## Purpose
 * Implements the MCP specification requirement for "sending messages to the server" by providing
 * an HTTP POST endpoint that processes client JSON-RPC 2.0 messages and maintains SSE connections
 * for server responses.
 *
 * ## Configuration
 *
 * The annotation automatically configures:
 * - **Endpoint**: `POST /mcp`
 * - **Request Content-Type**: `application/json` (for JSON-RPC messages)
 * - **Response Content-Type**: `text/event-stream` (for SSE responses)
 *
 * ## Session Support
 * The endpoint accepts an optional `Mcp-Session-Id` header (referenced by [SESSION_ID_HEADER])
 * when stateful sessions are used:
 *
 * ```kotlin
 * @McpPost
 * suspend fun handleMcpRequest(
 *   @RequestBody message: JSONRPCMessage,
 *   @RequestHeaders headers: Headers,
 *   sendChannel: SendChannel<ServerSentEvent>
 * ) {
 *   val sessionId = headers[SESSION_ID_PARAM]
 *   mcpStreamManager.withResponseChannel(sendChannel, sessionId) {
 *     // Handle the MCP JSON-RPC message
 *     handleMessage(message)
 *   }
 * }
 * ```
 *
 * ## Method Signature Requirements
 *
 * Methods annotated with `@McpPost` should typically have:
 * - `@RequestBody message: JSONRPCMessage` - For JSON-RPC 2.0 message processing
 * - `sendChannel: SendChannel<ServerSentEvent>` - For SSE responses
 * - `suspend` modifier for coroutine support
 * - Optional `@RequestHeaders headers: Headers` for session ID extraction
 *
 * ## MCP Protocol Support
 * Handles all standard MCP JSON-RPC 2.0 operations:
 * - **initialize**: Server capability negotiation
 * - **tools/list**: Available tool discovery
 * - **tools/call**: Tool execution requests
 * - **resources/list**: Available resource discovery
 * - **resources/read**: Resource content retrieval
 * - **prompts/list**: Available prompt discovery
 * - **prompts/get**: Prompt template retrieval
 *
 * ## Example Usage
 *
 * ```kotlin
 * @Singleton
 * class McpWebAction @Inject constructor(
 *   private val mcpStreamManager: McpStreamManager
 * ) : WebAction {
 *
 *   @McpPost
 *   suspend fun handleMcpRequest(
 *     @RequestBody message: JSONRPCMessage,
 *     @RequestHeaders headers: Headers,
 *     sendChannel: SendChannel<ServerSentEvent>
 *   ) {
 *     val sessionId = headers[SESSION_ID_PARAM]
 *     mcpStreamManager.withResponseChannel(sendChannel, sessionId) {
 *       // Process client JSON-RPC message and send response via SSE
 *       handleMessage(message)
 *     }
 *   }
 * }
 * ```
 *
 * @see McpGet for server-to-client event streaming
 * @see McpDelete for session termination
 * @see SESSION_ID_HEADER for the session ID header constant
 * @see McpStreamManager For managing MCP streams and server lifecycle
 * @see MiskMcpServer For the underlying MCP server implementation
 */
@Post("/mcp")
@RequestContentType(MediaTypes.APPLICATION_JSON)
@ResponseContentType(MediaTypes.SERVER_EVENT_STREAM)
@Target(AnnotationTarget.FUNCTION)
@ExperimentalMiskApi
annotation class McpPost
