package project00_Game;

import javax.swing.JButton;      // 버튼 컴포넌트
import javax.swing.JFrame;       // 메인 윈도우 창
import javax.swing.JPanel;       // 그림을 그릴 패널
import javax.swing.Timer;        // 일정 시간 간격으로 실행되는 타이머
import javax.swing.ImageIcon;    // image 생성을 위한 import 

import java.awt.BorderLayout;    // 컴포넌트를 상하좌우중앙에 배치하는 레이아웃
import java.awt.Color;           // 색상을 지정할 때 사용
import java.awt.Font;            // 글씨체와 크기를 설정
import java.awt.Graphics;        // 화면에 그림을 그릴 때 사용

import java.awt.event.ActionEvent;      // 이벤트 객체
import java.awt.event.ActionListener;   // 이벤트가 발생했을 때 동작 지정
import java.awt.Image;                  // Image 클래스를 사용하기 위한 임포트

import java.util.Random;         // 랜덤 숫자 생성을 위한 클래스

/*
 * 생성자 : 유연우
 * 생성일 : 25.04.28
 * 파일명 : HorseRaceGame.java
 * 수정자 : 
 * 수정일 :
 * 설명 : 경마 게임 실행
 */


// 메인 프레임 클래스 (게임 창 생성)
public class HorseRaceGame extends JFrame {

    private RacePanel racePanel;        // 말 경주가 표시되는 패널
    private JButton startButton;        // "경주 시작" 버튼
    
    public HorseRaceGame() {
        setTitle("말 경주 게임");                         // 창 제목 설정
        setSize(800, 400);                      // 창 크기 설정
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);      // 닫기 버튼 눌렀을 때 프로그램 종료
        setLayout(new BorderLayout());                       // 레이아웃 설정 (위/아래/왼/오/가운데)
        
        racePanel = new RacePanel();          // 경주 패널 생성
        add(racePanel, BorderLayout.CENTER);  // 경주 패널을 가운데에 추가
        
        startButton = new JButton("경주 시작!");                 // 시작 버튼 생성
        startButton.addActionListener(e -> racePanel.startRace());  // 버튼 누르면 경주 시작
        add(startButton, BorderLayout.SOUTH);                      // 버튼을 창 아래쪽에 배치
        
        setVisible(true);        // 창 보이게 하기
    }
    
   public static void main(String[] args) {
    new HorseRaceGame();          // 프로그램 실행 
   }
}

// 경주 패널 클래스 (말들을 그리는 곳)
class RacePanel extends JPanel {
    private int[] horseX = {0, 0, 0, 0, 0};  // 각 말의 X 좌표 위치 (초기값 0)
    private boolean running = false;        // 경주 중인지 여부 false = 경주중 아님
    private Timer timer;                    // 말 이동을 위한 타이머
    private Random random = new Random();   // 말 이동 거리를 랜덤하게 하기 위한 객체
    private String winner = "";             // 1등 말 텍스트 저장용 (초기화)
    private Image horseImage;               // 말 이미지를 저장하는 변수(private) -> horseImage

    public RacePanel() {
        setBackground(Color.WHITE);        // 배경색 흰색으로 설정
        ImageIcon icon = new ImageIcon("/Users/mac/Documents/YYW/project00_Game/horse.png"); //저장된 이미지 불러옴
        horseImage = icon.getImage(); // 저장된 말 이미지 호출
        
    }

    // 경주 시작 메서드
    public void startRace() {
        if (running) return;                // 이미 경주 중이면 중복 시작 방지

        winner = "";                        // 승자 초기화
        horseX = new int[]{0, 0, 0, 0, 0};  // 말 위치 초기화
        running = true;                     // 경주 중으로 표시

        // 타이머 생성: 0.1초(100ms)마다 실행
        timer = new Timer(100, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < horseX.length; i++) {
                    horseX[i] += random.nextInt(20);  // 0~19 사이의 거리만큼 전진

                    // 말이 화면 오른쪽 끝에 도달한 경우
                    if (horseX[i] >= getWidth() - 100 && running) {
                        running = false;                // 경주 종료
                        winner = "🏁 " + (i + 1) + "번 말이 1등입니다!";  // 승자 저장
                        timer.stop();                   // 타이머 멈추기
                        repaint();                      // 다시 그리기 (결과 출력)
                        break;
                    }
                }
                repaint();  // 화면 다시 그려서 말 위치 갱신
            }
        });

        timer.start();  // 타이머 시작 → 경주 시작
    }

    // 화면 그리는 함수 (말 그리기)
    protected void paintComponent(Graphics g) {
    
        super.paintComponent(g);
        // 배경색칠하기
        g.setColor(getBackground());

        // 트랙 선 칠하기
        g.setColor(Color.BLACK);
        for(int i =0; i<horseX.length;i++){
            int y = 60 + i * 50 - 20; // 말 위치보다 조금 위에 긋기
            g.drawLine(0, y, getWidth(),y); //(시작 x,y, 끝 x,y)
        }
        for (int i = 0; i < horseX.length; i++) {
            g.drawImage(horseImage, horseX[i], 40+i*50, 80, 40, this); // 말의 크기
            g.setColor(Color.BLACK);
            g.drawString((i + 1) + "번 말", 10, 60 + i * 50); // 말 이름 텍스트
        }

        // 승자 텍스트 출력
        if (!winner.isEmpty()) {
            g.setColor(Color.RED);
            g.setFont(new Font("굴림", Font.BOLD, 20));
            g.drawString(winner, 300, 30);  // 위쪽에 승자 표시
        }
    }
}
