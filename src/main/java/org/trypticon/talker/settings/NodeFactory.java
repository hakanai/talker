package org.trypticon.talker.settings;

public class NodeFactory {

//    public static Supplier<Node> createTwitchChat(SettingsGraph graph) {
//        return nodeBuilder(graph, "Twitch Chat")
//                .initialLocation(50, 50)
//                .outputConnector(ConnectorType.TEXT)
//                ::build;
//    }
//
////        add(nodeBuilder("Text Macros")
////                .initialLocation(100, 100)
////                .inputConnector(ConnectorType.TEXT)
////                .outputConnector(ConnectorType.TEXT)
////                .build());
//
////        add(nodeBuilder("Filter Text")
////                .initialLocation(150, 150)
////                .inputConnector(ConnectorType.TEXT)
////                .outputConnector(ConnectorType.TEXT)
////                .build());
//
//        add(nodeBuilder("MaryTTS")
//                .initialLocation(200, 200)
//                .inputConnector(ConnectorType.TEXT)
//                .outputConnector(ConnectorType.AUDIO)
//                .build());
//
//        add(nodeBuilder("Audio Player")
//                .initialLocation(250, 250)
//                .inputConnector(ConnectorType.AUDIO)
//                .build());
//    }
//
//    private void addSampleNodes2() {
//
//        add(nodeBuilder("Microphone")
//                .initialLocation(50, 50)
//                .outputConnector(ConnectorType.AUDIO)
//                .build());
//
//        add(nodeBuilder("Speech Recognition")
//                .initialLocation(100, 100)
//                .inputConnector(ConnectorType.AUDIO)
//                .outputConnector(ConnectorType.TEXT)
//                .build());
//
//        add(nodeBuilder("Filter Text")
//                .initialLocation(150, 150)
//                .inputConnector(ConnectorType.TEXT)
//                .outputConnector(ConnectorType.TEXT)
//                .build());
//
//        add(nodeBuilder("Text-to-Speech")
//                .initialLocation(200, 200)
//                .inputConnector(ConnectorType.TEXT)
//                .outputConnector(ConnectorType.AUDIO)
//                .build());
//
//        add(nodeBuilder("Audio Player")
//                .initialLocation(250, 250)
//                .inputConnector(ConnectorType.AUDIO)
//                .build());
//    }

    private static NodeBuilder nodeBuilder(SettingsGraph graph, String title) {
        return new NodeBuilder(graph, title);
    }

}
