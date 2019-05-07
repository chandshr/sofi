// @GENERATOR:play-routes-compiler
// @SOURCE:/home/chandani/data/projects/interview/sofi/conf/routes
// @DATE:Mon May 06 22:24:14 PDT 2019


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
