## SkyDelivery | Food Delivery System 

A self-practice full-stack food delivery system built with Spring Boot, featuring intelligent distance determination powered by Google Maps APIs, which is following along "sky_take_out" project.

JDK version: `11`
### Tech Stack 
#### Backend
- Spring Boot `2.7.3`
- Spring MVC
- MySQL
- Redis
- JWT Authentication
- Apache HttpClient

#### Dev Tools
- Maven 3.9.9
- MyBatis 2.2.0
- Lombok 1.18.30
- Fastjson 1.2.76
- Swagger 3.0
- Knife4j 3.0.2
- POI 3.16

### Key Features
- WeChat/Mobile authentication
- Geo-fence validation
- Jump payment (due to Wechat' limit policy)
- Order history management
####
- Data visualization using Echarts
- Business analytics dashboard and export to Excel 

### Google Maps API
- **Geocoding**: Convert addresses to GPS coordinates
- **Distance Calculation**: Get actual route distance via Directions API

### Deployment
1. Regester and apply for Alibaba cloud OSS, Wechat mini-program and Google maps API.
2. Deploy Front-end pages and start nginx service in advance.
3. add `application-dev.yml` file at `sky-server/src/main/resources/`
4. Start Spring Boot Application
```yaml
sky:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    host: localhost
    port: 3306
    database: sky_take_out
    username: root
    password: "YourPassword"

  jwt:
    admin-secret-key: itcast
    user-secret-key: itheima

  alioss:
    endpoint: YourEndpoint
    access-key-id: YourAccessKeyID
    access-key-secret: YourAccessKeyScrete
    bucket-name: YourBucketName
  redis:
    host: localhost
    port: 6379
    database: 1
    password: YourPassword

  wechat:
    appid: YourMiniProgramID
    secret: YourKeySecret

  shop:
    address: CustomAddress
  google:
    key: YourApiKey

```
---
