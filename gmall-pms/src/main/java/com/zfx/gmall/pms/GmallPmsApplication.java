package com.zfx.gmall.pms;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * 缓存的使用场景:
 *      一些固定的数据，不太变化的数据，高频访问的数据(基本不变)，变化频率低的都可以入缓存，加速系统的访问
 * 缓存的目的:提高系统查询效率，提供性能
 * 1)、将菜单缓存起来，以后查询直接去缓存中拿即可;
 *      设计模式:模板模式:
 *      操作xxx都有对应的xxxTemplate;
 * JdbcTemplate、RestTemplate、 Redis Template、MongoTemplate
 * RedisTemplate<Object, object>; k-v;
 * v有五种类型String、V
 *      StringRedisTemplate: k- v都是String的。
 *      引入-一个场景，猜这个场景的xxxAutoConfiguration,
 *      帮我们注入能操作这个技术的组件，这个场景的配置信息都在xxProperties中说明了(prefix = "spring.redis ")使用哪种前缀配置
 * 2)、整合Redis两大步
 *      1)、导入starter- data-redis
 *      2)、applicat ion. properties配置与spring. redis相关的
 *     注意:
 *          Redis Template;存数据默认使用jdk的方式序列化存过去。
 *              我们推荐都应该存成json;
 *          做法:
 *              将默认的序列化器改为json的|
 */
@EnableDubbo
@SpringBootApplication
@MapperScan("com.zfx.gmall.pms.mapper")
public class GmallPmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmallPmsApplication.class, args);
    }

}
