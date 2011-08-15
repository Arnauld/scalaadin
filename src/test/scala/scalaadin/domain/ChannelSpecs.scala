package scalaadin.domain

import org.specs.Specification
import org.specs.mock.Mocker
import collection.mutable.ListBuffer

class ChannelSpecs extends Specification with Mocker {

  "Default Channel names" should {
    "remain constants over time" in {
      Channel.LOG_TRACE must_== "log-trace"
      Channel.LOG_DEBUG must_== "log-debug"
      Channel.LOG_INFO  must_== "log-info"
      Channel.LOG_WARN  must_== "log-warn"
      Channel.LOG_ERROR must_== "log-error"
      Channel.DATA      must_== "data"
    }
  }

  "Channel" should {
    "have no endpoint by default" in {
      val channel = new Channel[String]() {
        def name = "test-channel"
      }
      channel.endpoints.isEmpty must_== true
    }
    "be able to add an endpoint!" in {
      val channel = new Channel[String]() {
        def name = "test-channel"
      }
      val endpoint = Endpoint("test-endpoint", { m:Message[String] => /*no op*/ })
      channel.addEndpoint(endpoint)
      channel.endpoints must_== List(endpoint)
    }
    "be able to add two endpoints!" in {
      val channel = new Channel[String]() {
        def name = "test-channel"
      }
      val buffer1 = ListBuffer[String]()
      val buffer2 = ListBuffer[String]()
      val endpoint1 = Endpoint("test-endpoint-1", { m:Message[String] => buffer1+=m.payload })
      val endpoint2 = Endpoint("test-endpoint-2", { m:Message[String] => buffer2+=m.payload })
      channel.addEndpoint(endpoint1)
      channel.addEndpoint(endpoint2)
      channel.endpoints must contain(endpoint1)
      channel.endpoints must contain(endpoint2)
    }
  }

  "Channel publish" should {
    "support having no endpoints" in {
      val channel = new Channel[String]() {
        def name = "test-channel"
      }
      channel.publishPayload("Hello")
      channel.publish(Message("Hello")).isExpectation     // appears as passed if no exception is thrown
    }
    "dispatch message to all of its endpoints" in {
      val buffer = ListBuffer[String]()
      val endpoint = Endpoint("test-endpoint", { m:Message[String] => buffer+=m.payload })
      val channel = new Channel[String]() {
        def name = "test-channel"
      }
      channel.addEndpoint(endpoint)
      channel.publish(Message("Hello!"))
      buffer must_== List("Hello!")
    }
    "dispatch payload to all of its endpoints" in {
      val buffer = ListBuffer[String]()
      val endpoint = Endpoint("test-endpoint", { m:Message[String] => buffer+=m.payload })
      val channel = new Channel[String]() {
        def name = "test-channel"
      }
      channel.addEndpoint(endpoint)
      channel.publishPayload("Hello!")
      buffer must_== List("Hello!")
    }
    "dispatch message to all of its endpoints" in {
      val buffer1 = ListBuffer[String]()
      val buffer2 = ListBuffer[String]()
      val endpoint1 = Endpoint("test-endpoint-1", { m:Message[String] => buffer1+=m.payload })
      val endpoint2 = Endpoint("test-endpoint-2", { m:Message[String] => buffer2+=m.payload })
      val channel = new Channel[String]() {
        def name = "test-channel"
      }
      channel.addEndpoint(endpoint1)
      channel.addEndpoint(endpoint2)
      channel.publish(Message("Hello!"))
      buffer1 must_== List("Hello!")
      buffer2 must_== List("Hello!")
    }

  }
}