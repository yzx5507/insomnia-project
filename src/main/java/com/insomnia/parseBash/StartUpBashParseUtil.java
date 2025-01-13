package com.insomnia.parseBash;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 启动参数解析
 *
 * @author Jay  zhixin.yuan@transsnet.com
 * @project: insomnia-project
 * @description:
 * @date 2024/10/31
 */
public class StartUpBashParseUtil {

    public static final String BASH = """
root           1       0 17 02:41 pts/0    00:07:15 java -Dfile.encoding=UTF-8 -Xms1024m -Xmx2048m -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5000 -javaagent:/pp-agent/arms-agent.jar -javaagent:/sandbox/lib/sandbox-agent.jar -javaagent:/jacoco/jacocoagent.jar=appname=posp-daily,cloud=aws,buildid=165838 -Ddc.source.consumes=H4sIAAAAAAAAAM1R0U6DMBT9lz4DgQx07o0AzsYNCEOTZVlIx+4WFFrWFuNi9u+2nfPJPemDD01u77nn3J6e1QciPWtbFrWDkMDRBG1hR4ZWIuuCMCqGDh7hqEDJCRWCgnR6cnQ4q19BdgenPs9wsfLW38QC9g2jihTO8PIpNf0eb9HEu/UtdKFMORtMF01xXOXZoooewjRNZlWJY8XZdzLiQCQYojcKvMB3Rzf+nYHmbNvsmp8wLVsee1DCZZQroUYNjcduoCoqJKE1mK28O9g1tQfyPgLYvGxcrmdFDC2Yna6FusOXUJYukL4+K6dna4Hj6o76ryahb5gqwo60AizEL+5hsGug6uNa21OzkvVNrR+V5TgyfrMiTgrttsLpfYZO1l9n4v4+E3Xy62EEwfUwDHYtDP9/hREuq7II40RX8yQtq2JWotP6E2slxIslAwAA -Ddc.nodes=H4sIAAAAAAAAAEtJNjQyN9BJSTY0NrYEUSaGIMrE1BwABAZXORoAAAA= -Ddc.env=dc1427 -Dapollo.cluster=dc1427 -Ddc.properties.rewrite=H4sIAAAAAAAAAKtWys8r1i0zVbKKjq0FAA/mzL8NAAAA -Denv=DAILY -Dserver.port=8080 -Dtranssnet.palmpay.aes.password=palmpay -Dspring.cloud.config.label=master -Dspring.cloud.nacos.config.server-addr=nacos-frankfurt-daily.palmpay-inc.com:8848 -Dspring.cloud.nacos.config.namespace=daily -Dspring.cloud.nacos.config.username=nacos-daily -Dspring.cloud.nacos.config.password=BbM2E%QnH@V?y_i,d?J4 -Dspring.cloud.config.uri=http://config-server:11111 -Denv=DAILY -Dapollo.meta=http://apollo-config-frankfurt-daily.palmpay-inc.com:8080 -Dspring.cloud.discovery.client.composite-indicator.enabled=false -Dmanagement.health.consul.enabled=false -Dmanagement.port=8081 -Dmanagement.context-path=/actuator -Dspring.cloud.consul.discovery.health-check-path=/actuator/health -Dspring.cloud.consul.discovery.hostname=172.22.36.41 -Dspring.cloud.consul.host=172.22.36.41 -jar posp.jar
root         584     577  0 03:24 pts/1    00:00:00 grep java
                        """;

    public static void main(String[] args) {
        Pattern pattern = Pattern.compile("-D[^ ]+");
        Matcher matcher = pattern.matcher(BASH);

        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            result.append(matcher.group() + "\n");
        }
        result.append("-Dtranssnet.palmpay.log.errorRootPath=/Users/yuanzhixin/logs\n" +
                "-Dtranssnet.palmpay.log.statRootPath=/Users/yuanzhixin/logs\n" +
                "-Dtranssnet.palmpay.log.rootPath=/Users/yuanzhixin/logs\n" +
                "-Dspring.cloud.consul.discovery.register=false");
        String replace = StringUtils.replace(result.toString(), "-Duser.home=/home/app\n", "");
        System.out.println(replace);
    }
}
