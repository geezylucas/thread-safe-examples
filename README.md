# Thread safety

## Atomic Variables

En Java, las variables atómicas residen en el paquete java.util.concurrent.atomic. Ofrecen una programación segura para hilos y sin bloqueos, lo que mejora la eficiencia y evita situaciones deadlock que podrían surgir con las técnicas de sincronización tradicionales.

Una operación atómica es indivisible. Esto significa que una vez que una operación comienza a ejecutarse, se ejecuta hasta su finalización sin ser interrumpida por otro hilo. Esta atomicidad es vital para la programación concurrente porque ayuda a prevenir race conditions.

### Operaciones atómicas

El algoritmo utiliza instrucciones de máquina de bajo nivel como compare-and-swap (CAS, compare-and-swap, que garantiza la integridad de los datos y ya hay una gran cantidad de investigación al respecto).

Una operación CAS típica opera con tres operandos:

- Espacio de memoria para el trabajo (M)
- Valor esperado existente (A) de una variable
- Nuevo valor (B) a configurar
- CAS actualiza atómicamente M a B, pero solo si el valor de M es el mismo que el de A; de lo contrario, no se realiza ninguna acción.

En el primer y segundo caso, se devolverá el valor de M. Esto le permite combinar tres pasos, a saber, obtener el valor, comparar el valor y actualizarlo. Y todo se convierte en una sola operación a nivel de máquina.

En el momento en que una aplicación de hilos múltiples accede a una variable e intenta actualizarla y se aplica CAS, uno de los hilos la obtendrá y podrá actualizarla. Pero a diferencia de los bloqueos, otros hilos simplemente obtendrán errores sobre la imposibilidad de actualizar el valor. Luego pasarán a trabajar más, y el cambio está completamente excluido en este tipo de trabajo.

En este caso, la lógica se vuelve más difícil debido al hecho de que tenemos que manejar la situación en la que la operación CAS no funcionó con éxito. Simplemente modelaremos el código para que no avance hasta que la operación tenga éxito.

### Java’s Atomic Classes

Java proporciona varias clases atómicas, como:

- AtomicInteger
- AtomicLong
- AtomicBoolean
- AtomicReference

Los principales métodos expuestos por estas clases son:

- get() – Obtiene el valor de la memoria, de modo que los cambios realizados por otros hilos sean visibles; equivalente a leer una variable volátil
- incrementAndGet() – Incrementa atómicamente en uno el valor actual
- set() – Escribe el valor en la memoria, de modo que el cambio sea visible para otros hilos; equivalente a escribir una variable volátil
- lazySet() – Eventualmente escribe el valor en la memoria, tal vez reordenándolo con operaciones de memoria relevantes subsiguientes. Un caso de uso es anular referencias, con el fin de la recolección de basura, a la que nunca se volverá a acceder. En este caso, se logra un mejor rendimiento al retrasar la escritura volátil nula
- compareAndSet() – Devuelve verdadero cuando tiene éxito, de lo contrario falso

## Thread-Local

ThreadLocal es una clase de Java que permite crear variables que solo pueden ser leídas y escritas por el mismo hilo. Esto puede ser útil en situaciones en las que varios hilos acceden a la misma variable, pero se desea garantizar que cada hilo tenga su propia copia aislada de la variable.

ThreadLocal se utiliza a menudo en situaciones en las que se tiene una aplicación con varios hilos y se desea almacenar el estado de cada hilo, como la información de inicio de sesión de un usuario o una conexión a una base de datos. Por ejemplo, en una aplicación web, cada solicitud es manejada por un hilo independiente, pero cada solicitud está asociada a un usuario específico. Al utilizar una variable ThreadLocal para almacenar la información del usuario, se puede garantizar que cada hilo solo tenga acceso a la información del usuario para la solicitud que está manejando.

Las variables Thread-Local de Java son una herramienta valiosa para administrar datos específicos de hilos, pero conllevan su propio conjunto de desafíos y problemas potenciales. Si conoce estos problemas comunes y sigue las mejores prácticas, puede usar las variables Thread-Local de manera efectiva en sus aplicaciones Java multiproceso.

Recuerde:

- Tenga en cuenta el consumo de memoria y limpie las variables Thread-Local cuando ya no sean necesarias.
- Comprenda que las variables Thread-Local no reemplazan la necesidad de una sincronización adecuada cuando se trabaja con recursos compartidos.
- Asegúrese de que los recursos asociados con las variables Thread-Local se liberen para evitar fugas de recursos.
- Resuelva los problemas de serialización reinicializando las variables Thread-Local en el nuevo hilo después de la deserialización.
- Evite el uso excesivo de las variables Thread-Local, ya que pueden acoplar hilos inadvertidamente y hacer que su código sea más complejo de lo necesario.

## 3 Problemas principales de concurrencia en Java: Deadlock, Starvation and Race Conditions

La concurrencia en Java permite a los desarrolladores escribir aplicaciones altamente eficientes y escalables al ejecutar múltiples hilos simultáneamente. Los beneficios de la programación concurrente son sustanciales. Presenta una serie de desafíos que pueden provocar un comportamiento impredecible, una degradación del rendimiento o un fallo total de la aplicación.

### ReentrantLock

La clase ReentrantLock forma parte del paquete java.util.concurrent.locks. Implementa la interfaz Lock, que define las operaciones básicas de un bloqueo: lock, unlock, tryLock y lockInterruptibly. Un objeto ReentrantLock tiene dos características principales: es reentrante y es justo. Reentrante significa que un hilo puede adquirir el mismo bloqueo varias veces sin bloquearse a sí mismo. Justo significa que el candado otorga acceso al hilo de espera más largo, en lugar del que llega primero.

#### ReentrantLock vs sincronizado

La palabra clave synchronized es otra forma de lograr la sincronización de hilos en Java, que se puede aplicar a un método o un bloque de código y utiliza un bloqueo implícito asociado con el objeto o la clase. En comparación con ReentrantLock, la palabra clave sincronizada es más restrictiva, ya que se bloquea y desbloquea automáticamente al principio y al final del ámbito, mientras que ReentrantLock le permite bloquear y desbloquear en cualquier punto de su código. Además, ReentrantLock proporciona más funciones, como interrumpir un hilo en espera, intentar adquirir un bloqueo durante un tiempo específico y comprobar si el bloqueo está sujeto por el hilo actual o cualquier hilo. Además, ReentrantLock puede ser justo o injusto, mientras que la palabra clave sincronizada siempre es injusta.

#### Cómo usar ReentrantLock

instancia de la misma y pasar un argumento booleano para indicar si es justa o injusta. Luego, debe llamar al método de bloqueo antes de acceder al recurso compartido y al método de desbloqueo después de liberarlo. También debe controlar cualquier excepción que pueda producirse, como InterruptedException. Por ejemplo, supongamos que tiene un contador compartido que necesita ser incrementado por varios hilos. Puede utilizar un ReentrantLock para sincronizar la operación de incremento de la siguiente manera:

```java
import java.util.concurrent.locks.ReentrantLock;

public class Counter {

  private int value;
  private ReentrantLock lock;

  public Counter(boolean fair) {
    value = 0;
    lock = new ReentrantLock(fair);
  }

  public void increment() {
    lock.lock();
    try {
      value++;
    } finally {
      lock.unlock();
    }
  }

  public int getValue() {
    return value;
  }
}        
```

### Deadlock

Se produce cuando dos o más hilos esperan que el otro libere los recursos que necesita, lo que provoca que todos permanezcan bloqueados indefinidamente. Imagine un escenario en el que el thread A tiene el bloqueo 1 y necesita el bloqueo 2 para continuar, mientras que el thread B tiene el bloqueo 2 y necesita el bloqueo 1. Ninguno puede continuar, lo que da como resultado un deadlock.

Estrategias para evitar deadlock:

- Lock ordering: Adquiera siempre bloqueos en un orden global coherente entre todos los hilos.
- Lock timeout: Utilice tiempos de espera al intentar adquirir bloqueos. Si no se adquiere un bloqueo dentro del tiempo de espera, el hilo puede liberar cualquier bloqueo retenido y volver a intentarlo, lo que reduce el riesgo de bloqueo.
- Try-Lock: `ReentrantLock` de Java ofrece un método tryLock() que intenta adquirir un bloqueo sin bloquearse indefinidamente, lo que permite que los hilos den marcha atrás y vuelvan a intentarlo o tomen acciones alternativas si el bloqueo no está disponible.

### Starvation

Ocurre cuando a uno o más hilos se les niega permanentemente el acceso a los recursos o al tiempo de CPU, generalmente porque otros hilos monopolizan estos recursos. Los hilos que sufren inanición no pueden continuar, lo que genera demoras significativas o una aplicación que no responde.

Estrategias para prevenir starvation:

- Fair locks: Utilice mecanismos de bloqueo justos donde los hilos adquieran bloqueos en el orden en que los solicitaron, como lo respalda `ReentrantLock` con el parámetro fairness.
- Thread priority: Use las prioridades de hilos de manera juiciosa. Depender en gran medida de las prioridades de hilos puede exacerbar la inanición, ya que es posible que los hiilos de menor prioridad nunca se ejecuten.
- Work distribution: Garantice una distribución justa del trabajo y el acceso a los recursos entre los hilos, posiblemente mediante el uso de construcciones de concurrencia de nivel superior como los ejecutores que administran los grupos de hilos de manera más eficiente.

### Race Conditions

Race conditions se producen cuando los hilo acceden y modifican datos compartidos simultáneamente, sin tener en cuenta la sincronización adecuada, lo que genera un estado de aplicación inconsistente o erróneo. El resultado suele ser impredecible y difícil de reproducir, lo que hace que las race conditions sean especialmente problemáticas de depurar.

Estrategias para prevenir race conditions:

- Synchronization: Utilice bloques o métodos sincronizados para garantizar que solo un subproceso pueda acceder a una sección crítica del código a la vez.
- Atomic variables: Utilice variables atómicas del paquete `java.util.concurrent.atomic` para operaciones que se deben realizar de forma atómica, sin necesidad de una sincronización explícita.
- Concurrent collections: Reemplace las recopilaciones estándar con versiones concurrentes del paquete `java.util.concurrent` que están diseñadas para gestionar el acceso concurrente de forma segura.

## Concurrent collections

Las Concurrent collections son parte del paquete java.util.concurrent, que proporciona varias clases de colección seguras para hilos. A diferencia de las colecciones tradicionales que se encuentran en java.util (como ArrayList y HashMap), las colecciones concurrentes están diseñadas para permitir que varios subprocesos accedan y modifiquen las colecciones simultáneamente sin la necesidad de una sincronización externa.

### Importancia de las colecciones concurrentes

En aplicaciones multiproceso, las colecciones tradicionales pueden generar problemas de concurrencia, race conditions y corrupción de datos, si no se sincronizan correctamente. Las colecciones concurrentes abordan estos problemas implementando operaciones seguras para hilos. Lo logran utilizando mecanismos de bloqueo o sin bloqueo de grano fino, lo que mejora significativamente el rendimiento y la escalabilidad en entornos multiproceso.

- ConcurrentHashMap: Una versión de HashMap segura para hilos que permite operaciones de lectura y escritura concurrentes. Es una opción ideal para administrar una estructura de mapa en contextos multiproceso.
- CopyOnWriteArrayList: Una implementación de List que realiza una copia nueva de la matriz subyacente con cada mutación. Es muy eficiente para escenarios donde la iteración es mucho más común que la modificación.
- BlockingQueue: Una interfaz que extiende Queue con operaciones que esperan a que la cola deje de estar vacía al recuperar un elemento y esperan a que haya espacio disponible en la cola al almacenar un elemento. ArrayBlockingQueue y LinkedBlockingQueue son implementaciones notables.
- ConcurrentLinkedQueue: Una opción adecuada para escenarios de alta concurrencia, proporciona una lista ordenada de elementos con operaciones eficientes sin bloqueo.
