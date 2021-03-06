package com.sd.lib.animator.listener;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * 动画监听，重写此类的方法要记得先调用super的方法
 */
public abstract class FAnimatorListener extends AnimatorListenerAdapter
{
    private WeakReference<View> mTarget;
    private WeakReference<Animator> mAnimator;

    public FAnimatorListener()
    {
    }

    public FAnimatorListener(View target)
    {
        setTarget(target);
    }

    /**
     * 设置target
     *
     * @param target
     */
    public final void setTarget(View target)
    {
        mTarget = target == null ? null : new WeakReference<>(target);
    }

    /**
     * 优先返回设置的target，如果为null的话，返回动画对象中的target
     *
     * @return
     */
    public final View getTarget()
    {
        final View targetSpec = mTarget == null ? null : mTarget.get();
        if (targetSpec != null)
            return targetSpec;

        return getAnimatorTarget();
    }

    /**
     * 返回动画对象中的target
     *
     * @return
     */
    public final View getAnimatorTarget()
    {
        final Animator animator = getAnimator();
        if (animator instanceof ObjectAnimator)
        {
            final Object target = ((ObjectAnimator) animator).getTarget();
            if (target instanceof View)
                return (View) target;
        }
        return null;
    }

    private void setAnimator(Animator animator)
    {
        final Animator old = getAnimator();
        if (old != animator)
            mAnimator = animator == null ? null : new WeakReference<>(animator);
    }

    private Animator getAnimator()
    {
        return mAnimator == null ? null : mAnimator.get();
    }

    @Override
    public void onAnimationStart(Animator animation)
    {
        super.onAnimationStart(animation);
        setAnimator(animation);
    }

    @Override
    public void onAnimationCancel(Animator animation)
    {
        super.onAnimationCancel(animation);
        setAnimator(animation);
    }

    @Override
    public void onAnimationEnd(Animator animation)
    {
        super.onAnimationEnd(animation);
        setAnimator(animation);
    }

    @Override
    public void onAnimationRepeat(Animator animation)
    {
        super.onAnimationRepeat(animation);
        setAnimator(animation);
    }

    @Override
    public void onAnimationPause(Animator animation)
    {
        super.onAnimationPause(animation);
        setAnimator(animation);
    }

    @Override
    public void onAnimationResume(Animator animation)
    {
        super.onAnimationResume(animation);
        setAnimator(animation);
    }
}
