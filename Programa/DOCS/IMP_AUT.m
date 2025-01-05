clc; clearvars; close all;

%% IMPLEMENTAÇÃO AUTOMÁTICA

% Parâmetros
m_s = 250;      % Massa suspensa (kg) - 250 a 500 kg 
m_u = 50;       % Massa não suspensa (kg) - 25 a 75 kg
k_s = 15000;    % Rigidez da suspensão (N/m) - 10 000 a 50 000 N/m
k_t = 20000;   % Rigidez do pneu (N/m) - 150 000 a 250 000 N/m
c_s = 1000;     % Amortecimento da suspensão (Ns/m) - 1 000 a 5 000 Ns/m

% Matrizes do Espaço de Estados
A = [0, 1, 0, 0;
    -k_s/m_s, -c_s/m_s, k_s/m_s, c_s/m_s;
     0, 0, 0, 1;
     k_s/m_u, c_s/m_u, -(k_s+k_t)/m_u, -c_s/m_u];
B = [0; 0; 0; k_t/m_u];
C = [1, 0, 0, 0; 0, 0, 1, 0];
D = [0; 0];

% Função do MATLAB (sys) para definir o Sistema das matrizes do Espaço de Estados
sys = ss(A, B, C, D);

% Parâmetros para Simulação
A = 0.1;        % Amplitude do solavanco, valor arbitrário
t = 0:0.01:10;   % Período/Tempo de 0 a 5 segundo com degrau (step) de 0.01 s

% Simulação
u = A * sin(2 * pi * t);        % Excitação da rua de 0.1 m (altura do solavanco)
[y, t, x] = lsim(sys, u, t);    % lsim é uma função linear do MATLAB para simular sistemas no domínio tempo

% --- Cálculo da Aceleração da Massa Suspensa ---

x_s = x(:,1);         % Deslocamento da massa suspensa
dx_s = x(:,2);        % Velocidade da massa suspensa
x_u = x(:,3);         % Deslocamento da massa não suspensa
dx_u = x(:,4);        % Velocidade da massa não suspensa

acel_suspensa = (-k_s * (x_s - x_u) - c_s * (dx_s - dx_u)) / m_s;  % Aceleração da massa suspensa

% --- Cálculo do Deslocamento da Suspensão ---
desl_susp = x_u - x_s;  % Deslocamento relativo (x_u - x_s)

% --- Plots ---

% Deslocamentos
figure;
subplot(2,1,1);
plot(t, y(:,1), 'LineWidth', 1.5);
title('Deslocamento da Massa Suspensa (x_s)');
xlabel('Tempo (s)');
ylabel('Deslocamento (m)');
grid on;

subplot(2,1,2);
plot(t, y(:,2), 'LineWidth', 1.5);
title('Deslocamento da Massa Não Suspensa (x_u)');
xlabel('Tempo (s)');
ylabel('Deslocamento (m)');
grid on;

% Aceleração e Deslocamento da Suspensão
figure;
subplot(2,1,1);
plot(t, acel_suspensa, 'LineWidth', 1.5);
title('Aceleração da Massa Suspensa (x''''_s)');
xlabel('Tempo (s)');
ylabel('Aceleração (m/s^2)');
grid on;

subplot(2,1,2);
plot(t, desl_susp, 'LineWidth', 1.5);
title('Deslocamento da Suspensão (x_u - x_s)');
xlabel('Tempo (s)');
ylabel('Deslocamento (m)');
grid on;

% Estatísticas de conforto (RMS da aceleração da massa suspensa)
rms_acel_suspensa = rms(acel_suspensa);
fprintf('RMS da Aceleração da Massa Suspensa (Conforto): %.4f m/s^2\n', rms_acel_suspensa);
max_acel_suspensa = max(abs(acel_suspensa));
fprintf('Pico da Aceleração da Massa Suspensa (Conforto): %.4f m\n', max_acel_suspensa);

% Pico do Deslocamento Relativo da Suspensão
max_desl_susp = max(abs(desl_susp));
fprintf('Pico do Deslocamento Relativo da Suspensão: %.4f m\n', max_desl_susp);
rms_desl_susp = rms(desl_susp);
fprintf('RMS do Deslocamento Relativo da Suspensão: %.4f m/s^2\n', rms_desl_susp);

% figure;
% plot(t, u, 'LineWidth', 1.5);
% title('Onda Senoidal (x_r)');
% xlabel('Tempo (s)');
% ylabel('Amplitude (m2)');
% grid on;