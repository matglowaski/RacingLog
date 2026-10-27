# Racing Log

**Racing Log** é um aplicativo Android desenvolvido em Kotlin com Jetpack Compose para jogadores de simuladores de corrida registrarem tempos de volta, acompanharem seu desempenho e identificarem oportunidades de melhoria.

Projeto acadêmico com arquitetura MVVM, injeção de dependências com Hilt e persistência em nuvem pelo Firebase.

## Funcionalidades

- Seleção de simuladores: **Le Mans Ultimate**, **Automobilista 2**, **iRacing** e **Assetto Corsa Competizione**.
- Registro de voltas por simulador, circuito e carro.
- Registro opcional de tempos dos três setores.
- Análise de desempenho: melhor volta, melhores setores, volta ideal e ganho potencial.
- Histórico de voltas registradas, com atualização e exclusão mediante confirmação.
- Armazenamento dos dados no **Cloud Firestore**, com autenticação anônima do Firebase.
- Interface escura inspirada no automobilismo, com detalhes em vermelho.

## Tecnologias

| Tecnologia | Finalidade |
| --- | --- |
| Kotlin | Linguagem de programação |
| Jetpack Compose e Material 3 | Interface do usuário |
| Navigation Compose | Navegação entre telas |
| MVVM, ViewModel e Repository | Organização da aplicação |
| Hilt | Injeção de dependências |
| Firebase Authentication | Autenticação anônima |
| Cloud Firestore | Persistência dos registros |
| Gradle e KSP | Build e processamento de anotações |

## Telas

### Início
Seleção do simulador e acesso às funcionalidades.

### Registrar
Cadastro de volta com simulador, pista, carro, tempo total e setores opcionais.

### Análise
Exibição da melhor volta, melhores setores, volta ideal, ganho potencial e quantidade de voltas.

### Histórico
Listagem de registros salvos, atualização e exclusão individual com confirmação.

## Arquitetura

```text
com.example.racinglog
├── data
│   ├── model
│   └── repository
├── navigation
├── ui
│   ├── screens
│   │   ├── home
│   │   ├── register
│   │   ├── analysis
│   │   └── history
│   └── theme
├── viewmodel
└── MainActivity.kt
```

Fluxo principal: **Compose → ViewModel → Repository → Firebase Firestore**.

## Como executar

1. Clone o repositório e abra o projeto no Android Studio.
2. Crie ou configure um projeto no [Firebase Console](https://console.firebase.google.com/).
3. Registre um aplicativo Android com o identificador `com.example.racinglog`.
4. Ative **Authentication → Sign-in method → Anonymous**.
5. Configure o **Cloud Firestore** e regras de acesso apropriadas para os usuários autenticados.
6. Baixe o `google-services.json` do seu projeto Firebase e coloque-o na pasta `app/`.
7. Sincronize o Gradle e execute em um emulador ou dispositivo Android compatível.

> O projeto precisa de uma configuração Firebase válida para autenticar e salvar registros. Não publique credenciais privadas, arquivos de assinatura ou segredos no repositório.

## Objetivo acadêmico

Demonstrar desenvolvimento de interfaces modernas com Jetpack Compose, navegação, persistência com Firebase, arquitetura MVVM e injeção de dependências com Hilt.

## Status

Versão acadêmica funcional, com cadastro, análise, consulta e exclusão de voltas implementados.
