import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Scanner;

public class TrafficLightControllerSwing {
    private int greenDuration;
    private int yellowDuration;
    private int redDuration;
    private String currentState;
    private boolean running;
    private TrafficLightPanel lightPanel;

    public TrafficLightControllerSwing(int greenDuration, int yellowDuration, int redDuration) {
        this.greenDuration = greenDuration;
        this.yellowDuration = yellowDuration;
        this.redDuration = redDuration;
        this.currentState = "RED"; // Initial state
        this.running = true;
    }

    // Method to start the traffic light cycle
    public void start() {
        while (running) {
            switch (currentState) {
                case "GREEN":
                    updateLights("GREEN");
                    try {
                        Thread.sleep(greenDuration * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    currentState = "YELLOW";
                    break;

                case "YELLOW":
                    updateLights("YELLOW");
                    try {
                        Thread.sleep(yellowDuration * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    currentState = "RED";
                    break;

                case "RED":
                    updateLights("RED");
                    try {
                        Thread.sleep(redDuration * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    currentState = "GREEN";
                    break;
            }
        }
    }

    // Method to update the lights based on the current state
    public void updateLights(String state) {
        SwingUtilities.invokeLater(() -> lightPanel.setCurrentState(state));
    }

    // Method to create and display the GUI
    public void createAndShowGUI() {
        // Create the main frame
        JFrame frame = new JFrame("Traffic Light Controller");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(200, 400);
        frame.setLayout(new BorderLayout());

        // Light panel to display traffic lights
        lightPanel = new TrafficLightPanel();
        frame.add(lightPanel, BorderLayout.CENTER);

        // Button to stop the cycle
        JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(e -> {
            running = false;
            frame.dispose();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(stopButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Show the frame
        frame.setVisible(true);
    }

    // Inner class to represent the traffic light panel with round lights
    class TrafficLightPanel extends JPanel {
        private String currentState = "RED"; // Initial state

        public TrafficLightPanel() {
            setPreferredSize(new Dimension(200, 300));
            setBackground(Color.BLACK);
        }

        public void setCurrentState(String state) {
            this.currentState = state;
            repaint(); // Redraw the panel when the state changes
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw the traffic lights
            drawLight(g2d, 75, 50, Color.RED, currentState.equals("RED"));
            drawLight(g2d, 75, 125, Color.YELLOW, currentState.equals("YELLOW"));
            drawLight(g2d, 75, 200, Color.GREEN, currentState.equals("GREEN"));
        }

        private void drawLight(Graphics2D g2d, int x, int y, Color color, boolean isOn) {
            g2d.setColor(isOn ? color : color.darker().darker());
            g2d.fillOval(x, y, 50, 50); // Draw a filled circle
        }
    }

    // Main method
    public static void main(String[] args) {
        // Get user input for durations
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter green light duration (in seconds): ");
        int greenDuration = scanner.nextInt();
        System.out.print("Enter yellow light duration (in seconds): ");
        int yellowDuration = scanner.nextInt();
        System.out.print("Enter red light duration (in seconds): ");
        int redDuration = scanner.nextInt();
        scanner.close();

        // Initialize the traffic light controller
        TrafficLightControllerSwing controller = new TrafficLightControllerSwing(greenDuration, yellowDuration, redDuration);

        // Create and show the GUI
        SwingUtilities.invokeLater(controller::createAndShowGUI);

        // Start the traffic light cycle in a new thread
        new Thread(controller::start).start();
    }
}