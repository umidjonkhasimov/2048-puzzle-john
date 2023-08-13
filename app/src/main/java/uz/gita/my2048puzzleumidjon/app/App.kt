package uz.gita.my2048puzzleumidjon.app

import android.app.Application
import uz.gita.my2048puzzleumidjon.utils.MySharedPref

class App : Application() {
    override fun onCreate() {
        MySharedPref.init(this)
        super.onCreate()
    }
}