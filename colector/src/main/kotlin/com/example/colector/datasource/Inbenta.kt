package com.example.colector.datasource

import org.apache.camel.builder.RouteBuilder
import org.springframework.stereotype.Component

@Component
class Inbenta : RouteBuilder() {

    // https://github.com/schibsted/jslt/blob/master/functions.md

    private val EVENT_K1 = "let session = from-json(.session_value)\n" +
            "{\"event_id\": .event_id,\"var1\": \$session.var1,\"var2\": \$session.var2,\"var3\": \$session.var3}"

    private val EVENT_K2 = "let session = from-json(.session_value)\n" +
            "{\"event_id\": .event_id,\"list1\": \$session.list1,\"name\": \$session.name}"

    override fun configure() {
        from("kafka:inbenta-in")
            .routeId("kafka-input")
            .to("direct:input")

        from("direct:input")
            .routeId("input")
            .multicast()
                .to("direct:inbenta-event")
                .choice()
                    .`when`().jsonpath("\$[?(@.session_key == 'K1')]")
                        .to("direct:inbenta-k1").endChoice()
                    .`when`().jsonpath("\$[?(@.session_key == 'K2')]")
                        .to("direct:inbenta-k2").endChoice()
            .otherwise()
                .log("Unknown key")

        from("direct:inbenta-event")
            .routeId("inbenta-event")
            .to("kafka:flink-event")

        from("direct:inbenta-k1")
            .routeId("inbenta-k1")
            .setHeader("CamelJsltString").constant(EVENT_K1)
            .to("jslt:dummy?allowTemplateFromHeader=true")
            .to("kafka:flink-event-k1")

        from("direct:inbenta-k2")
            .routeId("inbenta-k2")
            .setHeader("CamelJsltString").constant(EVENT_K2)
            .to("jslt:dummy?allowTemplateFromHeader=true")
            .to("kafka:flink-event-k2")
    }
}