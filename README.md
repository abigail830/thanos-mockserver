# Thanos MockServer

## How to use

1. 准备好契约(/resources/contracts)和接口定义(/resource/schemas)
2. 配置application.properties, 选择所需要的模式（默认Standalone)
3. 启动MockServerMain (如为Platform模式需要先启动ContractService)
4. Rest查询/apis/mappings获取TCP端口列表
5. 指向对应端口进行正常挡板使用

**Remark:**
* 可外部覆盖的配置：
    * platformMode（true/false）
    * contractService.baseHttpPath（路径信息如'http://localhost:8081'， platformMode时必须配置）
    * 如若没有配置则默认使用application.properties中的值


## Functions

* [x] 为每对生产者-消费者启动挡板服务。Startup diff MockServerThread, each thread serve for provider-consumer pair and via diff port
* [x] 能通过REST API查询TCP挡板服务端口。Port mapping could be get via HTTP GET via API, API doc could be found at root or http://localhost:8082/api.html
* [x] 支持Standalone单机模式，启动时文本获取契约信息，没有动态更新。When standalone mode, contract & schema would be load from file base and no dynamic update
* [x] REST API辅助工具：根据契约生成定长请求报文，根据接口定义解释定长报文。Also provided utils, said could generate request msg base on contract, and parse message base on schema.
* [x] 支持Platform平台模式，启动时从ContractService获取契约信息。 When non-standalone mode, contract & schema would be load from contract service HTTP, dynamic update is not implement yet.
* [x] Platform平台模式下，接收ContractService动态更新. Support dynamic update in platform mode 

#### TODO
* [ ] 预定义常用正则Validator。 Pre-define Regex Validator
* [ ] 预定义一个FuncValidator(如DateTime)。Pre-define a FuncValidator
* [ ] Platform模式下Rest请求ContractService需要添加重试机制. Enhance retry logic in platform mode
* [ ] 挡板服务启动时支持白名单、黑名单配置. Enhance with white list and black list config


## Component Diagram

<img src="https://plantuml-server.kkeisuke.app/svg/bLLBZzem4BxpArmEBPNQZLhrrDEosMS5smY2gfwGIGPmwzYfuooBglxtZXCxE663s5nYZsU--UPbNnQQAbrcVF26YOINAPIAawtMUV4r21JTNMoOtfQhiW2LIA51w8j4Pi6C9LIbyf513Zm8ltoCE1LwCPq46IqqA1GjGvdbKXYBt3XuHqg1fXaAYDu2MKlEvOw93U5C0A4Y9QrZfWfSIOAk2PS9vNoF68ucENQ0fvFnp--BU3cvkOsNiuUx-tWmY40LKeoGV55ce3vz8CC51ZF4_pKy1J4OkiFtNRtFU1xQzP7kV8-BR7dph8s-LZJHSr1FB87Qgf8GAqBzWP5HZ58jhsGfKghs8vepL9xAvBVHCx3axu3WrqGrsi5gMLI7g3b3vpzWPPMjO9opoXsc4jJ5B_f4tScwv6GSFP33CVAeuIsLwzKeG-yXJROLRVCtrjyiYg4sfNbkAZJNKa43uFWbniGBtDiUW8PMWlLMnCDyWSwQSRYY1JXAj_xU8CurTWdbw1sHqahaT4YJzhTaZkePFQARDRa-S9vsPJt4NuD-I7dBWLS9ByjMIWvwyLO1JK_WrmejR6lZuKPA9b0KIDtG1yoWvuBq-53OE25dkBHTL6zyt4UJ_6uTiCDdm7jIu84Ex-Cu8dStCMRt3lIOSvVN1v72C-Bqmnp5xyeKuau34R33UyE6rOPn2UjducWKL_kAIR01NTVPIKvowmo29JPEKt9ezdCYEmF2lnDE669CPA4P9skyT3MJmfgtcVhpZpNLBEcbRTKwaJP6Cqpm2QOjCyVn1PlE3T2z41ft4ucchcX7oMtuzTfNhZtxVtK3Uuz4w-urpMAQtsJLcw98AkVSUpPU6DGuZEfx-K-9BKYcnSQDZ3zntQVal6bv1S9PNJwu197YS_eV.svg">

Note: Async communication between threads which using Guava EventBus is not shown in above diagram
