# Animator
对ObjectAnimator和AnimatorSet进行封装

## Gradle
`implementation 'com.fanwe.android:animator:1.1.0-rc2'`

## 简单demo
效果图：<br>
![](http://thumbsnap.com/i/wOZ62RgA.gif?0814)<br>

```java
/**
 * x方向中心点对齐
 */
private final XCenterAligner mXCenterAligner = new XCenterAligner();
/**
 * y方向中心点对齐
 */
private final YCenterAligner mYCenterAligner = new YCenterAligner();

public void onClickBtnAnim(View v)
{
    /**
     * 创建一个节点动画
     */
    final NodeAnimator nodeAnimator = new FNodeAnimator();

    nodeAnimator
            /**
             * 设置要执行动画的view
             */
            .setTarget(v)

            /**
             * 设置动画view的x方向要移动到哪些view的位置
             * 第一个参数为动画view和对齐view的对齐方式，默认左边对齐，库中还提供了中心点对齐的实现类
             */
            .moveToX(mXCenterAligner, v, view_target_1, view_target_2, view_target_3).setDuration(2000)

            /**
             * chain()方法返回的是动画链对象
             *
             * 创建一个新的节点动画，新动画和上一个动画同时执行
             * 参数表示是否复制上一个动画的设置参数，比如动画时长等
             */
            .chain().nodeWith(true)

            /**
             * 设置动画view的y方向要移动到哪些view的位置
             * 第一个参数为动画view和对齐view的对齐方式，默认顶部对齐，库中还提供了中心点对齐的实现类
             */
            .moveToY(mYCenterAligner, v, view_target_1, view_target_2, view_target_3)

            /**
             * 延迟1000毫秒
             */
            .chain().nodeNext().setDuration(1000)

            /**
             * 添加一个动画监听
             * 库中默认提供了很多方便的动画监听，比如：
             * - OnStartVisible，动画开时的时候让某个view可见，默认的view是动画view
             * - OnEndRemoveView，动画结束后移除某个view，默认是移除动画view
             *
             * 注意：在哪个节点addListener，就是监听哪个节点的动画，demo这里监听的是延迟1000毫秒这个动画
             */
            .addListener(new OnEndReset()) // 动画完成后重置view

            /**
             * 开始执行整个动画链
             */
            .chain().start();

    /**
     * 为了解决view没办法超出父布局边界来执行动画的问题，提供了这个方法
     *
     * 内部实现原理：
     *
     * 1.对动画View截图然后设置给ImageView
     * 2.让镜像ImageView添加到android.R.id.content的FrameLayout里面，并对齐覆盖在动画view上面
     * 3.让镜像ImageView执行动画
     *
     * 注意：如果调用此方法的话，要记得在最后的动画结束后移除镜像ImageView，除非业务需求就是不移除
     */
    //    nodeAnimator.chain().startAsPop();

    AnimatorSet animatorSet = nodeAnimator.chain().toAnimatorSet(); // 也可以得到原生的动画对象
}
```
## 火箭动画demo
效果图：<br>
git太大了有点卡，具体可以看demo<br>
![](http://thumbsnap.com/i/cD2NW5lZ.gif?0815)<br>

```java
/**
 * true-调试模式，会输出整个动画链的结构，方便开发调试，可以给每个节点动画设置tag，来加强描述
 *
 * demo中的日志输入如下：
 *
 *    Head:火箭淡入
 *    Next:延迟500毫秒
 *    Next:开始数字缩放X With:开始数字缩放Y
 *    Next:火箭起飞 With:烟雾淡入
 *    Next:烟雾淡出
 *
 */
new FNodeAnimator(true)
        .setTarget(fl_rocket_root)
        .alpha(0, 1f).setDuration(500).setTag("火箭淡入")
        .chain().nodeNext().setDuration(500).setTag("延迟500毫秒")
        .chain().nodeNext().setTarget(tv_number).scaleX(1f, 0f).setRepeatCount(2).setDuration(1000).setTag("开始数字缩放X")
        .chain().nodeWith(true).scaleY(1f, 0f).setTag("开始数字缩放Y")
        .addListener(new FAnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {
                super.onAnimationStart(animation);
                tv_number.setText(String.valueOf(mNumber));
            }

            @Override
            public void onAnimationEnd(Animator animation)
            {
                super.onAnimationEnd(animation);
                mNumber = 3;
            }

            @Override
            public void onAnimationRepeat(Animator animation)
            {
                super.onAnimationRepeat(animation);
                mNumber--;
                tv_number.setText(String.valueOf(mNumber));
            }
        })
        .chain().nodeNext().setTarget(fl_rocket_root).translationY(0, -getResources().getDisplayMetrics().heightPixels).setTag("火箭起飞")
        .setDuration(3000).setInterpolator(new AccelerateInterpolator())
        .addListener(new OnEndInvisible(fl_rocket_root)) //动画结束隐藏fl_rocket_root
        .addListener(new OnEndReset(fl_rocket_root)) //动画结束重置fl_rocket_root
        .addListener(new FAnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {
                super.onAnimationStart(animation);
                AnimationDrawable animationDrawable = (AnimationDrawable) iv_rocket.getDrawable();
                animationDrawable.start(); //开始火箭喷火动画
            }

            @Override
            public void onAnimationEnd(Animator animation)
            {
                super.onAnimationEnd(animation);
                AnimationDrawable animationDrawable = (AnimationDrawable) iv_rocket.getDrawable();
                animationDrawable.stop(); //停止火箭喷火动画
            }
        })
        .chain().nodeWith().setTarget(iv_rocket_smoke).alpha(0, 1f).setDuration(3000).setStartDelay(500).setTag("烟雾淡入")
        .chain().nodeNext().alpha(1f, 0).setDuration(500).setTag("烟雾淡出")
        .addListener(new OnEndInvisible(iv_rocket_smoke)) //动画结束隐藏烟雾
        .chain().start();
```
