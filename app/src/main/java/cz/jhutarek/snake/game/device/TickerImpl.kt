package cz.jhutarek.snake.game.device

import cz.jhutarek.snake.game.domain.Ticker
import cz.jhutarek.snake.game.domain.TickerListener
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TickerImpl @Inject constructor() : Ticker {

    override var listener: TickerListener? = null

    private var disposable: Disposable? = null

    override fun start(intervalMillis: Long) {
        disposable = Observable.interval(intervalMillis, MILLISECONDS)
            .observeOn(mainThread())
            .subscribe { listener?.invoke() }
    }

    override fun stop() {
        disposable?.dispose()
        disposable = null
    }
}