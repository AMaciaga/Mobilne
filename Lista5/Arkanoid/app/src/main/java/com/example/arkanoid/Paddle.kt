package com.example.arkanoid

data class Paddle(var x: Float, var y: Float) {
    var dx = 0f
    val width = 125f
    val height = 25f
    val speed = 25f
}