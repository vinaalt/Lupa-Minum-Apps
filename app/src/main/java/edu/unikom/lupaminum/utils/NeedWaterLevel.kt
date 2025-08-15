package edu.unikom.lupaminum.utils

object NeedWaterLevel {
    fun needWaterLevel(temp: Double, humidity: Int): String {
        return if (temp < 86 && humidity < 50) {
            "Suhu dan Kelembapan Normal, Tapi kamu tetep harus minum ya!"
        } else if (temp < 90 && humidity < 60) {
            "Suhu dan Kelembapan masih Normal, Dengan minum air teratur kamu tidak akan kepanasan!"
        } else if (temp < 95 && humidity < 70) {
            "Hampir Bahaya! Kamu lagi dimana? Kok kamu di tempat bahaya sih! Kamu harus sering minum!"
        } else if (temp <= 100 && humidity <= 85) {
            "Bahaya! Minum sesering mungkin!"
        } else {
            "Sangat Bahaya! Mending kamu pergi dari tempat tersebut sambil minum"
        }
    }
}