## bug of import classes from other bundles

当引入了其他bundle中的类（在notification、DTCL，或者需要读其他yang文件定义的数据结构）时，

* 当前bundle的yang文件中定义的数据结构（container、rpc）不可用
* idea报错,但是能编译成功
```
The package 'org.opendaylight.yang.gen.v1.urn.opendaylight.packet.address.tracker.config.rev160621' is not exported by the bundle dependencies less... (Ctrl+F1) 

This inspections reports usage of classes from packages not accessible inside the OSGi context - i.e. those located in .jar files not packaged as bundles, or not exported by bundles, or not imported in manifest file (if applicable). Asking for such a classes may cause "class not found" exceptions at runtime.
The inspection is disabled in tests by default (see "Check tests" option).
```
* 查看karaf的log，发现如下报错：
```
2017-12-07 15:56:46,904 | WARN  | qtp600971144-453 | ServletHandler                   | 249 - org.eclipse.jetty.aggregate.jetty-all-server - 8.1.19.v20160209 | 
javax.servlet.ServletException: java.lang.UnsupportedOperationException: Not supported DataSchemaNode type class org.opendaylight.yangtools.yang.parser.stmt.rfc7950.effective.AnyDataEffectiveStatementImpl
	at org.apache.shiro.web.servlet.AdviceFilter.cleanup(AdviceFilter.java:196)[292:org.apache.shiro.web:1.3.2]
	at org.apache.shiro.web.servlet.AdviceFilter.doFilterInternal(AdviceFilter.java:148)[292:org.apache.shiro.web:1.3.2]
	at org.apache.shiro.web.servlet.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:125)[292:org.apache.shiro.web:1.3.2]
	at org.apache.shiro.web.servlet.ProxiedFilterChain.doFilter(ProxiedFilterChain.java:66)[292:org.apache.shiro.web:1.3.2]
	at org.apache.shiro.web.servlet.AdviceFilter.executeChain(AdviceFilter.java:108)[292:org.apache.shiro.web:1.3.2]
	at org.apache.shiro.web.servlet.AdviceFilter.doFilterInternal(AdviceFilter.java:137)[292:org.apache.shiro.web:1.3.2]
	at org.apache.shiro.web.servlet.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:125)[292:org.apache.shiro.web:1.3.2]
	at org.apache.shiro.web.servlet.ProxiedFilterChain.doFilter(ProxiedFilterChain.java:66)[292:org.apache.shiro.web:1.3.2]
	at org.apache.shiro.web.servlet.AbstractShiroFilter.executeChain(AbstractShiroFilter.java:449)[292:org.apache.shiro.web:1.3.2]
	at org.apache.shiro.web.servlet.AbstractShiroFilter$1.call(AbstractShiroFilter.java:365)[292:org.apache.shiro.web:1.3.2]
	at org.apache.shiro.subject.support.SubjectCallable.doCall(SubjectCallable.java:90)[291:org.apache.shiro.core:1.3.2]
	at org.apache.shiro.subject.support.SubjectCallable.call(SubjectCallable.java:83)[291:org.apache.shiro.core:1.3.2]
	at org.apache.shiro.subject.support.DelegatingSubject.execute(DelegatingSubject.java:383)[291:org.apache.shiro.core:1.3.2]
	at org.apache.shiro.web.servlet.AbstractShiroFilter.doFilterInternal(AbstractShiroFilter.java:362)[292:org.apache.shiro.web:1.3.2]
	at org.apache.shiro.web.servlet.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:125)[292:org.apache.shiro.web:1.3.2]
	at org.eclipse.jetty.servlet.ServletHandler$CachedChain.doFilter(ServletHandler.java:1478)[249:org.eclipse.jetty.aggregate.jetty-all-server:8.1.19.v20160209]
	at org.eclipse.jetty.servlet.ServletHandler.doHandle(ServletHandler.java:499)[249:org.eclipse.jetty.aggregate.jetty-all-server:8.1.19.v20160209]
	at org.ops4j.pax.web.service.jetty.internal.HttpServiceServletHandler.doHandle(HttpServiceServletHandler.java:69)[258:org.ops4j.pax.web.pax-web-jetty:3.2.9]
	at org.eclipse.jetty.server.handler.ScopedHandler.handle(ScopedHandler.java:137)[249:org.eclipse.jetty.aggregate.jetty-all-server:8.1.19.v20160209]
	at org.eclipse.jetty.security.SecurityHandler.handle(SecurityHandler.java:557)[249:org.eclipse.jetty.aggregate.jetty-all-server:8.1.19.v20160209]
	at org.eclipse.jetty.server.session.SessionHandler.doHandle(SessionHandler.java:231)[249:org.eclipse.jetty.aggregate.jetty-all-server:8.1.19.v20160209]
	at org.eclipse.jetty.server.handler.ContextHandler.doHandle(ContextHandler.java:1086)[249:org.eclipse.jetty.aggregate.jetty-all-server:8.1.19.v20160209]
	at org.ops4j.pax.web.service.jetty.internal.HttpServiceContext.doHandle(HttpServiceContext.java:240)[258:org.ops4j.pax.web.pax-web-jetty:3.2.9]
	at org.eclipse.jetty.servlet.ServletHandler.doScope(ServletHandler.java:427)[249:org.eclipse.jetty.aggregate.jetty-all-server:8.1.19.v20160209]
	at org.eclipse.jetty.server.session.SessionHandler.doScope(SessionHandler.java:193)[249:org.eclipse.jetty.aggregate.jetty-all-server:8.1.19.v20160209]
	at org.eclipse.jetty.server.handler.ContextHandler.doScope(ContextHandler.java:1020)[249:org.eclipse.jetty.aggregate.jetty-all-server:8.1.19.v20160209]
	at org.eclipse.jetty.server.handler.ScopedHandler.handle(ScopedHandler.java:135)[249:org.eclipse.jetty.aggregate.jetty-all-server:8.1.19.v20160209]
	at org.ops4j.pax.web.service.jetty.internal.JettyServerHandlerCollection.handle(JettyServerHandlerCollection.java:75)[258:org.ops4j.pax.web.pax-web-jetty:3.2.9]
	at org.eclipse.jetty.server.handler.HandlerWrapper.handle(HandlerWrapper.java:116)[249:org.eclipse.jetty.aggregate.jetty-all-server:8.1.19.v20160209]
	at org.eclipse.jetty.server.Server.handle(Server.java:370)[249:org.eclipse.jetty.aggregate.jetty-all-server:8.1.19.v20160209]
	at org.eclipse.jetty.server.AbstractHttpConnection.handleRequest(AbstractHttpConnection.java:494)[249:org.eclipse.jetty.aggregate.jetty-all-server:8.1.19.v20160209]
	at org.eclipse.jetty.server.AbstractHttpConnection.headerComplete(AbstractHttpConnection.java:973)[249:org.eclipse.jetty.aggregate.jetty-all-server:8.1.19.v20160209]
	at org.eclipse.jetty.server.AbstractHttpConnection$RequestHandler.headerComplete(AbstractHttpConnection.java:1035)[249:org.eclipse.jetty.aggregate.jetty-all-server:8.1.19.v20160209]
	at org.eclipse.jetty.http.HttpParser.parseNext(HttpParser.java:641)[249:org.eclipse.jetty.aggregate.jetty-all-server:8.1.19.v20160209]
	at org.eclipse.jetty.http.HttpParser.parseAvailable(HttpParser.java:231)[249:org.eclipse.jetty.aggregate.jetty-all-server:8.1.19.v20160209]
	at org.eclipse.jetty.server.AsyncHttpConnection.handle(AsyncHttpConnection.java:82)[249:org.eclipse.jetty.aggregate.jetty-all-server:8.1.19.v20160209]
	at org.eclipse.jetty.io.nio.SelectChannelEndPoint.handle(SelectChannelEndPoint.java:696)[249:org.eclipse.jetty.aggregate.jetty-all-server:8.1.19.v20160209]
	at org.eclipse.jetty.io.nio.SelectChannelEndPoint$1.run(SelectChannelEndPoint.java:53)[249:org.eclipse.jetty.aggregate.jetty-all-server:8.1.19.v20160209]
	at org.eclipse.jetty.util.thread.QueuedThreadPool.runJob(QueuedThreadPool.java:608)[249:org.eclipse.jetty.aggregate.jetty-all-server:8.1.19.v20160209]
	at org.eclipse.jetty.util.thread.QueuedThreadPool$3.run(QueuedThreadPool.java:543)[249:org.eclipse.jetty.aggregate.jetty-all-server:8.1.19.v20160209]
	at java.lang.Thread.run(Thread.java:748)[:1.8.0_131]
Caused by: java.lang.UnsupportedOperationException: Not supported DataSchemaNode type class org.opendaylight.yangtools.yang.parser.stmt.rfc7950.effective.AnyDataEffectiveStatementImpl
	at org.opendaylight.yangtools.yang.model.export.SchemaContextEmitter.emitDataSchemaNode(SchemaContextEmitter.java:232)[314:org.opendaylight.yangtools.yang-model-export:1.1.3.SNAPSHOT]
	at org.opendaylight.yangtools.yang.model.export.SchemaContextEmitter.emitDataNodeContainer(SchemaContextEmitter.java:206)[314:org.opendaylight.yangtools.yang-model-export:1.1.3.SNAPSHOT]
	at org.opendaylight.yangtools.yang.model.export.SchemaContextEmitter.emitList(SchemaContextEmitter.java:828)[314:org.opendaylight.yangtools.yang-model-export:1.1.3.SNAPSHOT]
	at org.opendaylight.yangtools.yang.model.export.SchemaContextEmitter.emitDataSchemaNode(SchemaContextEmitter.java:226)[314:org.opendaylight.yangtools.yang-model-export:1.1.3.SNAPSHOT]
	at org.opendaylight.yangtools.yang.model.export.SchemaContextEmitter.emitDataNodeContainer(SchemaContextEmitter.java:206)[314:org.opendaylight.yangtools.yang-model-export:1.1.3.SNAPSHOT]
	at org.opendaylight.yangtools.yang.model.export.SchemaContextEmitter.emitContainer(SchemaContextEmitter.java:766)[314:org.opendaylight.yangtools.yang-model-export:1.1.3.SNAPSHOT]
	at org.opendaylight.yangtools.yang.model.export.SchemaContextEmitter.emitDataSchemaNode(SchemaContextEmitter.java:220)[314:org.opendaylight.yangtools.yang-model-export:1.1.3.SNAPSHOT]
	at org.opendaylight.yangtools.yang.model.export.SchemaContextEmitter.emitDataNodeContainer(SchemaContextEmitter.java:206)[314:org.opendaylight.yangtools.yang-model-export:1.1.3.SNAPSHOT]
	at org.opendaylight.yangtools.yang.model.export.SchemaContextEmitter.emitGrouping(SchemaContextEmitter.java:749)[314:org.opendaylight.yangtools.yang-model-export:1.1.3.SNAPSHOT]
	at org.opendaylight.yangtools.yang.model.export.SchemaContextEmitter.emitDataNodeContainer(SchemaContextEmitter.java:203)[314:org.opendaylight.yangtools.yang-model-export:1.1.3.SNAPSHOT]
	at org.opendaylight.yangtools.yang.model.export.SchemaContextEmitter.emitBodyNodes(SchemaContextEmitter.java:181)[314:org.opendaylight.yangtools.yang-model-export:1.1.3.SNAPSHOT]
	at org.opendaylight.yangtools.yang.model.export.SchemaContextEmitter.emitModule(SchemaContextEmitter.java:112)[314:org.opendaylight.yangtools.yang-model-export:1.1.3.SNAPSHOT]
	at org.opendaylight.yangtools.yang.model.export.SchemaContextEmitter.writeToStatementWriter(SchemaContextEmitter.java:103)[314:org.opendaylight.yangtools.yang-model-export:1.1.3.SNAPSHOT]
	at org.opendaylight.yangtools.yang.model.export.YinExportUtils.writeModuleToOutputStream(YinExportUtils.java:83)[314:org.opendaylight.yangtools.yang-model-export:1.1.3.SNAPSHOT]
	at org.opendaylight.yangtools.yang.model.export.YinExportUtils.writeModuleToOutputStream(YinExportUtils.java:75)[314:org.opendaylight.yangtools.yang-model-export:1.1.3.SNAPSHOT]
	at org.opendaylight.netconf.md.sal.rest.schema.SchemaExportContentYinBodyWriter.writeTo(SchemaExportContentYinBodyWriter.java:47)[313:org.opendaylight.netconf.sal-rest-connector:1.5.3.SNAPSHOT]
	at org.opendaylight.netconf.md.sal.rest.schema.SchemaExportContentYinBodyWriter.writeTo(SchemaExportContentYinBodyWriter.java:25)[313:org.opendaylight.netconf.sal-rest-connector:1.5.3.SNAPSHOT]
	at com.sun.jersey.spi.container.ContainerResponse.write(ContainerResponse.java:306)[240:com.sun.jersey.jersey-server:1.17.0]
	at com.sun.jersey.server.impl.application.WebApplicationImpl._handleRequest(WebApplicationImpl.java:1479)[240:com.sun.jersey.jersey-server:1.17.0]
	at com.sun.jersey.server.impl.application.WebApplicationImpl.handleRequest(WebApplicationImpl.java:1391)[240:com.sun.jersey.jersey-server:1.17.0]
	at com.sun.jersey.server.impl.application.WebApplicationImpl.handleRequest(WebApplicationImpl.java:1381)[240:com.sun.jersey.jersey-server:1.17.0]
	at com.sun.jersey.spi.container.servlet.WebComponent.service(WebComponent.java:416)[280:com.sun.jersey.servlet:1.17.0]
	at com.sun.jersey.spi.container.servlet.ServletContainer.service(ServletContainer.java:538)[280:com.sun.jersey.servlet:1.17.0]
	at com.sun.jersey.spi.container.servlet.ServletContainer.service(ServletContainer.java:716)[280:com.sun.jersey.servlet:1.17.0]
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:668)[244:org.apache.geronimo.specs.geronimo-servlet_3.0_spec:1.0.0]
	at org.eclipse.jetty.servlet.ServletHolder.handle(ServletHolder.java:684)[249:org.eclipse.jetty.aggregate.jetty-all-server:8.1.19.v20160209]
	at org.eclipse.jetty.servlet.ServletHandler$CachedChain.doFilter(ServletHandler.java:1507)[249:org.eclipse.jetty.aggregate.jetty-all-server:8.1.19.v20160209]
	at org.eclipse.jetty.servlets.CrossOriginFilter.handle(CrossOriginFilter.java:247)[249:org.eclipse.jetty.aggregate.jetty-all-server:8.1.19.v20160209]
	at org.eclipse.jetty.servlets.CrossOriginFilter.doFilter(CrossOriginFilter.java:210)[249:org.eclipse.jetty.aggregate.jetty-all-server:8.1.19.v20160209]
	at org.eclipse.jetty.servlet.ServletHandler$CachedChain.doFilter(ServletHandler.java:1478)[249:org.eclipse.jetty.aggregate.jetty-all-server:8.1.19.v20160209]
	at org.eclipse.jetty.servlets.UserAgentFilter.doFilter(UserAgentFilter.java:82)[249:org.eclipse.jetty.aggregate.jetty-all-server:8.1.19.v20160209]
	at org.eclipse.jetty.servlets.GzipFilter.doFilter(GzipFilter.java:294)[249:org.eclipse.jetty.aggregate.jetty-all-server:8.1.19.v20160209]
	at org.eclipse.jetty.servlet.ServletHandler$CachedChain.doFilter(ServletHandler.java:1478)[249:org.eclipse.jetty.aggregate.jetty-all-server:8.1.19.v20160209]
	at org.opendaylight.aaa.filterchain.filters.CustomFilterAdapter.doFilter(CustomFilterAdapter.java:83)[296:org.opendaylight.aaa.filterchain:0.5.3.SNAPSHOT]
	at org.eclipse.jetty.servlet.ServletHandler$CachedChain.doFilter(ServletHandler.java:1478)[249:org.eclipse.jetty.aggregate.jetty-all-server:8.1.19.v20160209]
	at org.apache.shiro.web.servlet.ProxiedFilterChain.doFilter(ProxiedFilterChain.java:61)[292:org.apache.shiro.web:1.3.2]
	at org.apache.shiro.web.servlet.AdviceFilter.executeChain(AdviceFilter.java:108)[292:org.apache.shiro.web:1.3.2]
	at org.apache.shiro.web.servlet.AdviceFilter.doFilterInternal(AdviceFilter.java:137)[292:org.apache.shiro.web:1.3.2]
	... 39 more
2017-12-07 15:56:46,905 | WARN  | qtp600971144-453 | ServletHandler                   | 249 - org.eclipse.jetty.aggregate.jetty-all-server - 8.1.19.v20160209 | Error Processing URI: /restconf/modules/module/ietf-restconf/2017-01-26/schema - (java.lang.UnsupportedOperationException) Not supported DataSchemaNode type class org.opendaylight.yangtools.yang.parser.stmt.rfc7950.effective.AnyDataEffectiveStatementImpl
```
