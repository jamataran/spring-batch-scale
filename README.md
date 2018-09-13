# spring-batch-scale
Pruebas de concepto para escalado horizontal de procesos Spring Batch utilizando JMS como canal conductor.

Spring Batch nos ofrence las características de grandes procesados de datos. Para hacer un escalado horizontal, a través de varios noodos, necesitamos usar un _middleware_, en mi ejemplo voy a usar una implementación de **Cola JMS**.

A nivel de arquitectura, la siguiente imagen podría resumir lo que voy a tratar de implementar.

![Arquitectura escalado](http://www.ontheserverside.com/images/batch-scaling-strategies/remote-chunking.svg)