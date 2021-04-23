package tudbut.net.http;

/**
 * Class to build HTTP responses
 */
public class HTTPResponseFactory {
    
    /**
     * Response codes to HTTPRequests
     */
    public enum ResponseCode {
        Continue(100),
        SwitchingProtocols(101),
        Processing(102),
        EarlyHints(103),

        OK(200),
        Created(201),
        Accepted(202),
        NonAuthoritativeInformation(203),
        NoContent(204),
        ResetContent(205),
        PartialContent(206),
        MultiStatus(207),
        AlreadyReported(208),
        IM_Used(226),

        MultipleChoices(300),
        MovedPermanently(301),
        MovedTemporarily(302),
        SeeOther(303),
        NotModified(304),
        UseProxy(305),
        RESERVED__SwitchProxy(306),
        TemporaryRedirect(307),
        PermanentRedirect(308),

        BadRequest(400),
        Unauthorized(401),
        PaymentRequired(402),
        Forbidden(403),
        NotFound(404),
        MethodNotAllowed(405),
        NotAcceptable(406),
        ProxyAuthenticationRequired(407),
        RequestTimeout(408),
        Conflict(409),
        Gone(410),
        LengthRequired(411),
        PreconditionFailed(412),
        PayloadTooLarge(413),
        URI_TooLong(414),
        UnsupportedMediaType(415),
        RangeNotSatisfiable(416),
        ExpectationFailed(417),
        MisdirectedRequest(421),
        UnprocessableEntity(422),
        Locked(423),
        FailedDependency(424),
        TooEarly(425),
        UpgradeRequired(426),
        PreconditionRequired(428),
        TooManyRequests(429),
        RequestHeaderFieldsTooLarge(431),
        UnavailableForLegalReasons(451),

        InternalServerError(500),
        NotImplemented(501),
        BadGateway(502),
        ServiceUnavailable(503),
        GatewayTimeout(504),
        HTTPVersionNotSupported(505),
        VariantAlsoNegotiates(506),
        InsufficientStorage(507),
        LoopDetected(508),
        BandwidthLimitExceeded(509),
        NotExtended(510),
        NetworkAuthenticationRequired(511),
        ;
    
        /**
         * The id
         */
        public final int asInt;

        ResponseCode(int asIntIn) {
            asInt = asIntIn;
        }
    }
    
    /**
     * Creates a HTTPResponse
     * @param responseCode {@link HTTPResponseFactory.ResponseCode}
     * @param body The body of the request
     * @param contentType {@link HTTPContentType}
     * @param headers {@link HTTPHeader} Headers for the response
     * @return The constructed {@link HTTPResponse}
     */
    public static HTTPResponse create(ResponseCode responseCode, String body, HTTPContentType contentType, HTTPHeader... headers) {
        StringBuilder builder = new StringBuilder();

        builder.append("HTTP/1.1 ").append(responseCode.asInt).append(" ").append(responseCode.name()).append("\r\n");
        builder.append(new HTTPHeader("Content-Type", contentType.asHeaderString).toString()).append("\r\n");
        builder.append(new HTTPHeader("Content-Length", String.valueOf(body.length())).toString()).append("\r\n");
        for (HTTPHeader header : headers) {
            builder.append(header.toString()).append("\r\n");
        }
        builder.append("\r\n");
        builder.append(body);


        return new HTTPResponse(builder.toString());
    }
}
