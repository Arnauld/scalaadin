package scalaadin.security.shiro

import org.apache.shiro.realm.AuthorizingRealm
import org.apache.shiro.authc._
import org.apache.shiro.subject.PrincipalCollection
import org.apache.shiro.authz.{SimpleAuthorizationInfo, AuthorizationInfo}

class ScalaadinRealm extends AuthorizingRealm {

  override protected def doGetAuthenticationInfo(token: AuthenticationToken): AuthenticationInfo = {
    val upToken = token.asInstanceOf[UsernamePasswordToken]

    val username = upToken.getUsername
    checkNotNull(username, "Null usernames are not allowed by this realm.")

    val password = if(username=="warp") "none" else null
    checkNotNull(password, "No account found for user [" + username + "]")

    new SimpleAuthenticationInfo(username, password.toCharArray, getName)
  }


  def doGetAuthorizationInfo(principals: PrincipalCollection):AuthorizationInfo = {
    checkNotNull(principals, "PrincipalCollection method argument cannot be null.")

    import scala.collection.JavaConversions._
    val username = principals.getPrimaryPrincipal.asInstanceOf[String]
    val info = new SimpleAuthorizationInfo(rolesOf(username))
    info.setStringPermissions(permissionsOf(username))
    info
  }

  private def permissionsOf(username:String):Set[String] = Set()
  private def rolesOf(username:String):Set[String] = Set()

  private def checkNotNull(reference: AnyRef, message: String) {
    if (reference == null) {
      throw new AuthenticationException(message)
    }
  }
}