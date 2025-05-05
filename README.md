<body>
    <h1>Sistema de Detecção de Fraudes - Banco Digital</h1>
    <div class="highlight">
        <p>Sistema desenvolvido para validação em massa de CPFs utilizando múltiplas configurações de threads, com relatórios de desempenho comparativo.</p>
    </div>
    <h2>📋 Requisitos</h2>
    <ul>
        <li>Java JDK 11 ou superior</li>
        <li>30 arquivos de texto com CPFs (1 por dia)</li>
    </ul>
    <h2>🚀 Como Executar</h2>
    <h3>1. Configuração Inicial</h3>
    <pre><code>git clone https://github.com/itsryu/AT1_N1_PCD.git
cd AT1_N1_PCD</code></pre>
    <h3>2. Preparar Arquivos de Entrada</h3>
    <p>Modifique a pasta <code>data/</code> com os 30 arquivos de CPFs:</p>
    <h3>3. Compilar e Executar</h3>
    <div class="note">
        <p><strong>Observação:</strong> O sistema executará automaticamente todas as configurações de threads (1, 2, 3, 5, 6, 10, 15 e 30 threads).</p>
    </div>
    <h2>📊 Saída do Sistema</h2>
    <p>Os resultados serão gerados na pasta <code>output/</code> com os seguintes arquivos:</p>
    <ul>
        <li><code>v1_thread.txt</code></li>
        <li><code>v2_threads.txt</code></li>
        <li><code>v3_threads.txt</code></li>
        <li>...</li>
        <li><code>v30_threads.txt</code></li>
    </ul>
    <h2>🧠 Arquitetura do Sistema</h2>
    <h3>Principais Componentes</h3>
    <ul>
        <li><strong>Validação de CPF:</strong> Implementação do algoritmo oficial da Receita Federal</li>
        <li><strong>Processamento Paralelo:</strong> Múltiplas configurações de threads para análise comparativa</li>
        <li><strong>Gerenciamento de Arquivos:</strong> Leitura eficiente de grandes volumes de dados</li>
        <li><strong>Geração de Relatórios:</strong> Métricas de desempenho e resultados da validação</li>
    </ul>
    <h3>Diagrama Simplificado</h3>
    <pre>
    +-------------------+     +-------------------+     +-------------------+
    |   Arquivos de     |     |   Validador de    |     |   Processador     |
    |   Entrada (CPFs)  | --> |   CPF (Strategy   | --> |   Paralelo (N     |
    |                   |     |   Pattern)        |     |   threads)        |
    +-------------------+     +-------------------+     +-------------------+
                                                                    |
                                                                    v
                                                        +-------------------+
                                                        |   Gerador de      |
                                                        |   Relatórios      |
                                                        +-------------------+
    </pre>
</body>
