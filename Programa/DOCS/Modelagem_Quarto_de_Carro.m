clc; clearvars; close all;

%% IMPLEMENTAÇÃO AUTOMÁTICA

% Parâmetros
m_s = 250;      % Massa suspensa (kg) - 250 a 500 kg 
m_u = 50;       % Massa não suspensa (kg) - 25 a 75 kg
k_s = 15000;    % Rigidez da suspensão (N/m) - 10 000 a 50 000 N/m
k_t = 200000;   % Rigidez do pneu (N/m) - 150 000 a 250 000 N/m
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
t = 0:0.01:5;   % Período/Tempo de 0 a 5 segundo com degrau (step) de 0.01 s

% Simulação
u = A * sin(2 * pi * t);        % Excitação da rua de 0.1 m (altura do solavanco)
[y, t, x] = lsim(sys, u, t);    % lsim é uma função linear do MATLAB para simular sistemas no domínio tempo

% Resultados - basicamente aqui são funções e operações do MATLAB, deve ter semelhante no JAVA
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

% Dados calculados pela simulação para exportar ao JAVA
% csvwrite('resultados.csv', [t', y]); % Salva o tempo e os deslocamentos em um arquivo CSV


%% IMPLEMENTAÇÃO MANUAL
% 
% Definindo matrizes com valores arbitrários
% A = [0, 1; -2, -3];
% B = [0; 1];
% C = [1, 0];
% D = [0];
% 
% % Definindo o vetor tempo e a entrada
% dt = 0.01;               % Passo do tempo
% t = 0:dt:5;              % Vetor de tempo
% u = sin(2*pi*t);         % Sinal de Entrada (u) (senóide)
% x0 = [0; 0];             % Estado inicial (partindo do repouso)
% 
% % Realocando arrays para estado e saída do sistema
% x = zeros(2, length(t)); % Vetor de estados
% y = zeros(1, length(t)); % Vetor de saída
% 
% % Condições iniciais
% x(:, 1) = x0;
% 
% % Simulação
% for k = 1:length(t)-1
%     % Derivada do estado
%     dx = A*x(:, k) + B*u(k);
% 
%     % Usando o método de Euler
%     x(:, k+1) = x(:, k) + dx*dt;
% 
%     % Computando a saída
%     y(k) = C*x(:, k) + D*u(k);
% end
% 
% % Resultados
% figure;
% subplot(2,1,1);
% plot(t, u, 'r');
% title('Sinal de Entrada (forças externas)');
% xlabel('Tempo (s)');
% ylabel('u(t)');
% 
% subplot(2,1,2);
% plot(t, y, 'b');
% title('Resposta de Saída');
% xlabel('Tempo (s)');
% ylabel('y(t)');
% grid on;