//
//  WCStoreAppApp.swift
//  WCStoreApp
//
//  Created by Hicham Boushaba on 10/3/2022.
//

import SwiftUI
import WCStoreAppKmm
import class NavigationStack.NavigationStack
import struct NavigationStack.NavigationStackView

@main
struct WCStoreAppApp: App {
    @ObservedObject var navigationStack: NavigationStack
    
    private var navigationManager: IOSNavigationManager
    init() {
#if DEBUG
        LoggerCompanion.shared.installLogger(logger: IOSLogger(minLogPriority: LogPriority.debug))
#endif
        
        let navigationStack = NavigationStack()
        self.navigationStack = navigationStack
        navigationManager = IOSNavigationManager(navigationStack: navigationStack)
        KoinKt.doInitKoin(navigationManager: navigationManager)
    }
    
    var body: some Scene {
        WindowGroup {
            NavigationView {
                NavigationStackView(navigationStack: navigationStack) {
                    // Start with an empty view, to allow setting a destinationId to the Root view, check onAppear
                    EmptyView()
                }
                .toolbar(content: {
                    ToolbarItem(id: "BackButton", placement: .navigationBarLeading, showsByDefault: true) {
                        if (navigationStack.depth > 0) {
                            Button(action: {
                                navigationManager.navigateUp()
                            }, label: {
                                Image(systemName: "chevron.left")
                            }) } else {
                                EmptyView()
                            }
                    }
                })
            }.onAppear {
                navigationStack.push(MainScreen(), withId: KMMScreen.Home.shared.route)
            }
        }
    }
}
