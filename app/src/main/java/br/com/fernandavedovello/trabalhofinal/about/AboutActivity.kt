package br.com.fernandavedovello.trabalhofinal.about

import android.os.Bundle
import br.com.fernandavedovello.trabalhofinal.AppMenu
import br.com.fernandavedovello.trabalhofinal.R

class AboutActivity : AppMenu() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
    }

}
