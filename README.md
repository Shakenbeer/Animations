_So, I've decided to improve my animations._

### Compose Strikethrough Icon

![target](https://github.com/Shakenbeer/Animations/blob/master/strikethru.gif)

It's basically a copy-paste from a great article of Mark Allison: [Compose: Strikethru Animation](https://blog.stylingandroid.com/compose-strikethru-animation/)

**Pro-tips:**

If you want to draw on a Canvas as in good old days - ```DrawScope``` is you friend. Also, an animation progress should be a lambda, not a state -- more details on this in the article.

### Compose Pager

![target](https://github.com/Shakenbeer/Animations/blob/master/owls.gif)

**Pro-tips:**

I've implemented such behavior few years ago with RecyclerView. Compose code take much less space. But it's really hard to find out, how to do it with compose. 
[Pager from accompanist](https://google.github.io/accompanist/pager/) is used. And there is [calculateCurrentOffsetForPage](https://google.github.io/accompanist/api/pager/pager/com.google.accompanist.pager/calculate-current-offset-for-page.html), that speaks for itself.

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

_And here is the second one, could be loading indicator for flights booking app. Try it, in real life it looks smooth, somehow converting to GIF makes it uglier._

### Flight

![target](https://github.com/Shakenbeer/Animations/blob/master/flight.gif)

**Pro-tips:**

To tint bitmap but keep transparent background - use [PorterDuffColorFilter](https://developer.android.com/reference/android/graphics/PorterDuffColorFilter)

In the same time, if you don't want to see anything (line in this case) behind transparent background - use [PorterDuffXfermode](https://developer.android.com/reference/android/graphics/PorterDuffXfermode)

And, don't forget to set software layer type to your view, in other case PorterDuffXfermode will make background black.

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


_I've found next animation on [uplabs.com](https://www.uplabs.com/posts/checkbox-loader), [Roy Tan](https://www.uplabs.com/royrt88) implemented it with CSS and JS, I've decided to try animated vector drawable._

### Checkbox loader

![target](https://github.com/Shakenbeer/Animations/blob/master/checkbox_loader.gif)

**Further reading:**

Whole work could be done in a single xml file, you need kotlin (java) only to start animation. Read more: [Vector drawables overview](https://developer.android.com/guide/topics/graphics/vector-drawable-resources), [AnimatedVectorDrawable](https://developer.android.com/reference/android/graphics/drawable/AnimatedVectorDrawable)

This example was inspired by [Nick Butcher](https://medium.com/@crafty)'s series on vector assets in Android.

### Progress Ring

![target](https://github.com/Shakenbeer/Animations/blob/master/progress_ring.gif)

**Pro-tips:**

When you are using PorterDuff, everything, what is already on a canvas, is considered as destination. To separate progress background and use only progress itself as destination (e.g. as mask for a gradient), one could use `Canvas.saveLayer()` and `canvas.restore`

Glow effect is reachable simply with `Paint.setShadowLayer()`

**Further reading:**

This example is compilation of [A glowing progress ring with rounded ends for Android](https://medium.com/glose-team/a-glowing-progress-ring-with-rounded-ends-for-android-865eb0161cc1) and [this answer on SO](https://stackoverflow.com/questions/36639660/android-circular-progress-bar-with-rounded-corners/53830379#53830379)
