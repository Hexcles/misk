package misk.aws2.sqs.jobqueue

import misk.ReadyService
import misk.ServiceModule
import misk.annotation.ExperimentalMiskApi
import misk.aws2.sqs.jobqueue.config.SqsConfig
import misk.inject.KAbstractModule
import misk.jobqueue.QueueName
import misk.jobqueue.v2.JobHandler
import kotlin.reflect.KClass

/**
 * Install this module to register a handler for an SQS queue
 */
@ExperimentalMiskApi
class SqsJobHandlerModule private constructor(
  private val queueName: QueueName,
  private val handler: KClass<out JobHandler>,
) : KAbstractModule() {
  override fun configure() {
    newMapBinder<QueueName, JobHandler>().addBinding(queueName).to(handler.java)

    install(ServiceModule<SubscriptionService>().dependsOn<ReadyService>())
  }

  companion object {
    inline fun <reified T: JobHandler> create(queueName: String): SqsJobHandlerModule {
      return create(QueueName(queueName), T::class)
    }

    inline fun <reified T: JobHandler> create(queueName: QueueName): SqsJobHandlerModule {
      return create(queueName, T::class)
    }

    fun create(queueName: QueueName, handler: KClass<out JobHandler>) = SqsJobHandlerModule(queueName, handler)
  }
}
