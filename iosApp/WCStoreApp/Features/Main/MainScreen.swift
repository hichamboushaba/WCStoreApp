//
//  MainScreen.swift
//  WCStoreApp
//
//  Created by Hicham on 12/9/2022.
//

import SwiftUI
import WCStoreAppKmm
import KMPNativeCoroutinesCombine


struct MainScreen: View {
    private let viewModel = KoinKt.get(objCClass: MainViewModel.self) as! MainViewModel
    @FlowWrapper private var uiState: MainViewModel.UiState
    
    init() {
        _uiState = FlowWrapper(viewModel.uiStateNative, initialValue: viewModel.uiStateNativeValue)
    }
    
    var body: some View {
        Screen(hasNavigationBar: false) {
            TabView {
                HomeScreen()
                    .tabItem {
                        Label("Home", systemImage: "list.dash")
                    }
                HomeScreen()
                    .tabItem {
                        Label("Home 2", systemImage: "square.and.pencil")
                    }
            }
        }
    }
}
