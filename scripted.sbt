scriptedLaunchOpts ++= Seq(
  "-Xmx1024M",
  "-Dplugin.version=" + version.value,
  "-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005"
)

scriptedBufferLog := false
