package com.example.arkanoid

data class Ball(var x: Float, var y: Float) {
    var dx = 0f
    var dy = 0f
    val radius = 25f
}