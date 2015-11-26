package project.hjking.cn.myview.heikestyleview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class HkTextGroup extends View {

    //显示的字母数组
    private char[] counts = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
            'J', 'K', 'L', 'M', 'N', 'O'};

    private Paint paint;//画笔

    private int textSize = 20;//字体的大小（单位是像素）
    private Context ctx;
    private Cell[][] cells;
    private int row = 60;//要显示的行数
    private int list = 60;//要显示的列数
    public float left;
    public float left_bottom;

    public HkTextGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        ctx = context;
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setTextSize(textSize);//PX值
        paint.setTextAlign(Align.LEFT);
        paint.setStyle(Style.FILL);

        Log.i("list", "list::" + list + "  row::" + row);
        cells = new Cell[list][row];
        for (int j = 0; j < list; j++) {
            for (int i = 0; i < row; i++) {
                cells[j][i] = new Cell(i, j);
                cells[j][i].alpha = 0;
                cells[j][i].msg = "" + counts[(int) (Math.random() * counts.length)];
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        textSize = getWidth() / 10;
        left_bottom = getHeight();
    }



    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int flag = msg.what;// 0 -- 10

            for (int j = 0; j < list; j++) {

                for (int i = row - 1; i >= 0; i--) {
                    //1、如果第一行透明度为0，则有一定机率变为255
                    //2、如果中间行透明度为0，不做处理
                    //3、中间行不为0，依次减少一个梯度
                    //4、我上面的一个是255，那么我也是255,而他亮度减1
                    Cell cell = cells[j][i];

                    if (i == 0) {
                        if (cell.alpha == 0) {
                            if (Math.random() * 10 > 9) {
                                cell.alpha = 255;
                            }
                        } else {
                            cell.alpha = cell.alpha - 25 > 0 ? cell.alpha - 25 : 0;
                        }
                    } else if (i > 0 && i <= row - 1) {
                        if (cells[j][i - 1].alpha == 255) {
                            cell.alpha = 255;
                        } else {
                            cell.alpha = cell.alpha - 25 > 0 ? cell.alpha - 25 : 0;
                        }
                    }
                }
            }
            invalidate();

        }

        ;
    };

    private int seed = 0;
    private int stepCount = 11;
    @Override
    protected void onDraw(Canvas canvas) {

        for (int j = 0; j < list; j++) {
            for (int i = 0; i < row; i++) {
                Cell cell = cells[j][i];
                //小机率事件，改变内容
                if (Math.random() * 100 > 85) {
                    cell.msg = "" + counts[(int) (Math.random() * counts.length)];
                }
                //根据透明度确定颜色
                if (cell.alpha == 255) {
                    paint.setColor(Color.WHITE);
                } else {
                    paint.setColor(Color.GREEN);
                }
                //设置透明度
                paint.setAlpha(cell.alpha);
                //绘制
                if (cell.alpha != 0) {
                    canvas.drawText(cell.msg,
                            cell.j * 15,(float)(cell.i * textSize * 0.6 + textSize),
                            paint);
                }
            }
        }

//		seed = (seed + 1) % stepCount;
        handler.sendEmptyMessageDelayed(seed, 10);
    }


    /**
     *
     * 该类是单个的字母类
     * 包含了它的相关属性
     *
     */
    private class Cell {
        public Cell(int i, int j) {
            super();
            this.i = i;
            this.j = j;
        }

        public int i;//该字母所在的行
        public int j;//该字母所在的列
        public int seed;//种子
        public int alpha;//该字母的透明度
        public String msg;//该字母的内容

    }

}
