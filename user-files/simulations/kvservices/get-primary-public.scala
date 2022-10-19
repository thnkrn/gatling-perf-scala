package kvservice

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class Get extends Simulation {

  val feeder = csv("multi-db-60pct.txt").random

  val httpProtocol = http
    // Here is the root for all relative URLs
    .baseUrl("http://localhost:8080/api/databases")
    // Here are the common headers
    .header("Content-Type", "application/json")
    .shareConnections
    .maxConnectionsPerHost(150)

  // A scenario is a chain of requests and pauses
  val scn = scenario("Get primary on public IP")
    .repeat(50000) {
      feed(feeder)
        .exec(
          http("get")
            .get("/${db}/keys/${key}")
        )
    }

  // setUp(scn.inject(constantUsersPerSec(5).during(20.minutes)).protocols(httpProtocol))
  setUp(scn.inject(atOnceUsers(128)).protocols(httpProtocol))
}
