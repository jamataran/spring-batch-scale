# spring-batch-scale
Pruebas de concepto para escalado horizontal de procesos Spring Batch utilizando JMS como canal conductor.

Spring Batch nos ofrence las características de grandes procesados de datos. Para hacer un escalado horizontal, a través de varios noodos, necesitamos usar un _middleware_, en mi ejemplo voy a usar una implementación de **Cola JMS**.
