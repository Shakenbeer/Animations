So, I've decided to improve my animations. Here is the first one, for my biathlon pet-project.

### Biathlon target

![target](https://github.com/Shakenbeer/Animations/blob/master/biathlon_target.gif)

**Pro-tips:**

Use [AnimatorSet](https://developer.android.com/reference/android/animation/AnimatorSet) to chain [animations](https://developer.android.com/reference/android/animation/Animator)

```kotlin
    private val animations = mutableListOf<ValueAnimator>()
    private val animatorSet = AnimatorSet()

    private fun startAnimation() {
        val list = animations as List<Animator>?
        animatorSet.playSequentially(list)
        animatorSet.start()
    }
```

And here is the second one, could be loading indicator for flights booking app. Try it, in real life it looks smooth, somehow converting to GIF makes it uglier.

### Flight

![target](https://github.com/Shakenbeer/Animations/blob/master/flight.gif)

**Pro-tips:**

To tint bitmap but keep transparent background - use [PorterDuffColorFilter](https://developer.android.com/reference/android/graphics/PorterDuffColorFilter)

In the same time, if you don't want to see anything (line in this case) behind transparent background - use [PorterDuffXfermode](https://developer.android.com/reference/android/graphics/PorterDuffXfermode)

And, don't forget to set software layer type to your view, in other case PorterDuffXfermode with make background black.

```kotlin
    private val planeBitmap: Bitmap
    private val planePaint = Paint()
    //...
    init {
        planeBitmap = BitmapFactory.decodeResource(resources, R.drawable.airplane_white_48dp)
        planePaint.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
        planePaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }
```

Read more about [PorterDuff.Mode](https://developer.android.com/reference/android/graphics/PorterDuff.Mode)