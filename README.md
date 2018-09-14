# spring-batch-scale
Pruebas de concepto para escalado horizontal de procesos Spring Batch utilizando JMS como canal conductor.

Spring Batch nos ofrence las características de grandes procesados de datos. Para hacer un escalado horizontal, a través de varios noodos, necesitamos usar un _middleware_, en mi ejemplo voy a usar una implementación de **Cola JMS**.

A nivel de arquitectura, la siguiente imagen podría resumir lo que voy a tratar de implementar.

![Arquitectura escalado](http://www.ontheserverside.com/images/batch-scaling-strategies/remote-chunking.svg)

## Requisitos previos.
Para ejecutar el proyecto necesitaremos:
* Una implementación de JMS, en mi caso usaré ```activeMQ```, para instalar y activar en MacOs:
```bash
 sudo chown -R $(whoami) /usr/local/share/man/man7
 brew install activemq
 brew services start activemq
```

## Documentación relacionada. Bibliografía
* [http://www.ontheserverside.com/blog/2014/07/23/horizontal-and-vertical-scaling-strategies-for-batch-applications](http://www.ontheserverside.com/blog/2014/07/23/horizontal-and-vertical-scaling-strategies-for-batch-applications)
* [https://chelseatroy.com/2016/05/25/spring-batch-with-java-config-an-example/](https://chelseatroy.com/2016/05/25/spring-batch-with-java-config-an-example/)
* [https://blog.mimacom.com/trigger-spring-batch-job-with-jms-message/](https://blog.mimacom.com/trigger-spring-batch-job-with-jms-message/)
* [https://docs.spring.io/spring-batch/4.1.x/reference/html/spring-batch-integration.html#remote-chunking](https://docs.spring.io/spring-batch/4.1.x/reference/html/spring-batch-integration.html#remote-chunking)