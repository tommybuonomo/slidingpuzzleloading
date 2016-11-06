package com.tbuonomo.slidingpuzzleloading;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by tommy on 03/11/16.
 */

public class SlidingPuzzleView extends FrameLayout {
    private static final float WIDTH_SPACE = 6;
    private static final float HEIGHT_SPACE = 4;
    private static final double SQUARE_SIZE_FORMULA = Math.sqrt(2);

    private static final int DEFAULT_SQUARES_COLOR = Color.parseColor("#2196F3");
    private static final int DEFAULT_STROKE_COLOR = Color.parseColor("#FFFFFF");

    private float squareSize;
    private float space;

    private ImageView[] squaresImageViews;
    private AnimatorSet animatorSet;
    private int stepDuration = 500;
    private int strokeWidth;

    private boolean start;

    private GradientDrawable squareDrawable;

    private enum Square {
        LEFT(2, 1, 3, 2, 2, 3, 1, 2),
        RIGHT(4, 1, 3, 2, 4, 3, 5, 2);

        private int[] positions;

        Square(int ... positions) {
            this.positions = positions;
        }
    }

    public SlidingPuzzleView(Context context) {
        super(context);
        init(null);
    }

    public SlidingPuzzleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SlidingPuzzleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        squaresImageViews = new ImageView[6];
        squareDrawable = (GradientDrawable) ContextCompat.getDrawable(getContext(), R.drawable.puzzle_item_background);
        if (attrs != null) {
            TypedArray ta = getResources().obtainAttributes(attrs, R.styleable.SlidingPuzzleView);
            squareDrawable.setColor(ta.getColor(R.styleable.SlidingPuzzleView_squaresColor, DEFAULT_SQUARES_COLOR));

            strokeWidth = (int) ta.getDimension(R.styleable.SlidingPuzzleView_strokeWidth, dpToPixel(2));
            squareDrawable.setStroke(strokeWidth,
                    ta.getColor(R.styleable.SlidingPuzzleView_strokeColor, DEFAULT_STROKE_COLOR));
            squareDrawable.setCornerRadius(ta.getDimension(R.styleable.SlidingPuzzleView_cornerRadius, dpToPixel(3)));
            stepDuration = ta.getInteger(R.styleable.SlidingPuzzleView_animationDuration, 500);

            ta.recycle();
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        space = Math.min(w / WIDTH_SPACE, h / HEIGHT_SPACE);
        squareSize = (float) (space * SQUARE_SIZE_FORMULA) - dpToPixel(10);

        setUpSquares();
        setUpAnimators();
    }

    private void setUpSquares() {
        buildSquare(0, 2, 1);
        buildSquare(1, 4, 1);
        buildSquare(2, 1, 2);
        buildSquare(3, 5, 2);
        buildSquare(4, 2, 3);
        buildSquare(5, 4, 3);
    }

    private void setUpAnimators() {
        final AnimatorSet animatorSetLeft = buildAnimatorSet(squaresImageViews[0], squaresImageViews[2], squaresImageViews[4], Square.LEFT.positions);
        animatorSetLeft.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ((ValueAnimator) animatorSetLeft.getChildAnimations().get(0)).setCurrentPlayTime(0);
                ((ValueAnimator) animatorSetLeft.getChildAnimations().get(2)).setCurrentPlayTime(0);
                ((ValueAnimator) animatorSetLeft.getChildAnimations().get(3)).setCurrentPlayTime(0);
                animatorSetLeft.setStartDelay(stepDuration / 2);
                animatorSetLeft.start();
            }
        });

        final AnimatorSet animatorSetRight = buildAnimatorSet(squaresImageViews[1], squaresImageViews[3], squaresImageViews[5], Square.RIGHT.positions);
        animatorSetRight.setStartDelay(stepDuration * 5 / 4);
        animatorSetRight.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ((ValueAnimator) animatorSetRight.getChildAnimations().get(0)).setCurrentPlayTime(0);
                ((ValueAnimator) animatorSetRight.getChildAnimations().get(2)).setCurrentPlayTime(0);
                ((ValueAnimator) animatorSetRight.getChildAnimations().get(3)).setCurrentPlayTime(0);
                animatorSetRight.setStartDelay(stepDuration / 2);
                animatorSetRight.start();
            }
        });


        animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorSetLeft, animatorSetRight);

        if (start) {
            animatorSet.start();
        }
    }

    private void buildSquare(int index, int xPos, int yPos) {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageDrawable(squareDrawable);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) squareSize, (int) squareSize);
        params.leftMargin = (int) getSquarePosition(xPos);
        params.topMargin = (int) getSquarePosition(yPos);
        imageView.setLayoutParams(params);
        imageView.setRotation(45);

        squaresImageViews[index] = imageView;
        addView(imageView);
    }

    private float getSquarePosition(int pos) {
        return pos * space - squareSize / 2;
    }

    private AnimatorSet buildAnimatorSet(ImageView view1, ImageView view2, ImageView view3, int[] positions) {
        final ValueAnimator valueAnimator1 = buildAnimator(view1,
                getSquarePosition(positions[0]),
                getSquarePosition(positions[1]),
                getSquarePosition(positions[2]),
                getSquarePosition(positions[3]));

        final ValueAnimator valueAnimator2 = buildAnimator(view1,
                getSquarePosition(positions[2]),
                getSquarePosition(positions[3]),
                getSquarePosition(positions[4]),
                getSquarePosition(positions[5]));
        valueAnimator2.setStartDelay(stepDuration);

        final ValueAnimator valueAnimator3 = buildAnimator(view2,
                getSquarePosition(positions[6]),
                getSquarePosition(positions[7]),
                getSquarePosition(positions[0]),
                getSquarePosition(positions[1]));
        valueAnimator3.setStartDelay(stepDuration / 2);

        final ValueAnimator valueAnimator4 = buildAnimator(view3,
                getSquarePosition(positions[4]),
                getSquarePosition(positions[5]),
                getSquarePosition(positions[6]),
                getSquarePosition(positions[7]));
        valueAnimator4.setStartDelay(stepDuration * 3 / 4);


        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(valueAnimator1,
                valueAnimator2,
                valueAnimator3,
                valueAnimator4);

        return animatorSet;
    }

    private ValueAnimator buildAnimator(final ImageView view, float xFrom, float yFrom, float xTo, float yTo) {
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new PointEvaluator(), new PointF(xFrom, yFrom), new PointF(xTo, yTo));
        valueAnimator.setDuration(stepDuration);
        valueAnimator.setInterpolator(PathInterpolatorCompat.create(0.85f, 0, 0.25f, 1f));
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                PointF p = (PointF) valueAnimator.getAnimatedValue();
                LayoutParams params = (LayoutParams) view.getLayoutParams();
                params.leftMargin = (int) p.x;
                params.topMargin = (int) p.y;
                view.setLayoutParams(params);
            }
        });
        return valueAnimator;
    }

    private float dpToPixel(float dp) {
        return dp * getResources().getDisplayMetrics().density;
    }

    private class PointEvaluator implements TypeEvaluator<PointF> {
        private PointF p = new PointF();
        @Override
        public PointF evaluate(float v, PointF from, PointF to) {
            p.x = (int) (from.x + (to.x - from.x) * v);
            p.y = (int) (from.y + (to.y - from.y) * v);
            return p;
        }
    }

    private void reset() {
        stop();
        removeAllViews();
        setUpSquares();
        setUpAnimators();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        for (Animator animator : animatorSet.getChildAnimations()) {
            animator.end();
        }
        animatorSet.end();
    }


    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == View.VISIBLE) {
            if (start) {
                stop();
                start();
            }
        }
    }


    /*--------------PUBLIC METHODS---------------*/

    public void start() {
        if (animatorSet != null) {
            reset();
            animatorSet.start();
        }
        start = true;
    }

    public void stop() {
        if (animatorSet != null) {
            for (Animator animator : animatorSet.getChildAnimations()) {
                animator.end();
            }
            animatorSet.end();
        }
        start = false;
    }

    public void setSquaresColor(int color) {
        squareDrawable.setColor(color);
    }

    public void setSquaresStrokeColor(int color) {
        squareDrawable.setStroke(strokeWidth, color);
    }
}
