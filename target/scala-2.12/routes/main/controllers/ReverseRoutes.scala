// @GENERATOR:play-routes-compiler
// @SOURCE:/home/chandani/data/projects/interview/sofi/conf/routes
// @DATE:Mon May 06 22:24:14 PDT 2019

import play.api.mvc.Call


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:6
package controllers {

  // @LINE:6
  class ReverseHomeController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:6
    def index(): Call = {
      
      Call("GET", _prefix)
    }
  
  }

  // @LINE:8
  class ReverseUserController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:10
    def getAll(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "getAll")
    }
  
    // @LINE:8
    def add(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "add")
    }
  
  }


}
