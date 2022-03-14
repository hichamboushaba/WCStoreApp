//
//  NavigationManager.swift
//  WCStoreApp
//
//  Created by Hicham Boushaba on 14/3/2022.
//

import Foundation
import NavigationStack
import WCStoreAppKmm
import SwiftUI

class IOSNavigationManager: NavigationManager, ObservableObject {
    private let navigationStack: NavigationStack
    
    init(navigationStack: NavigationStack) {
        self.navigationStack = navigationStack
    }
    
    func navigate(route: String) {
        let iOSScreen = IOSScreen.allCases.first(where: { route.getBaseRoute() == $0.kmmScreen.baseRoute })!
        
        let kmmScreen = iOSScreen.kmmScreen
        
        let arguments = route.getArguments(screen: kmmScreen)
        
        let view = getView(iOSScreen: iOSScreen, arguments: arguments)
        
        navigationStack.push(view, withId: route)
    }
    
    func navigateUp() {
        navigationStack.pop()
    }
    
    func popUpTo(route: String, inclusive: Bool) {
        navigationStack.pop(to: PopDestination.view(withId: route))
    }
    
    @ViewBuilder
    func getView(iOSScreen: IOSScreen, arguments: Dictionary<String, String>) -> some View {
        switch iOSScreen {
        case .Home:
            HomeScreen()
        case .Product:
            ProductScreen(productId: Int64(arguments["productId"]!)!)
        }
    }
}

extension String {
    func getBaseRoute() -> String {
        if let index = lastIndex(of: "/") {
            return String(prefix(upTo: index))
        }
        return self
    }
    
    func getArguments(screen: Screen) -> Dictionary<String, String> {
        if (screen.navArguments.isEmpty) {
            return [:]
        }
        let routeTemplateParts = URLComponents(string: screen.route.replacingOccurrences(of: "[{,}]", with: "", options: .regularExpression))
        let routeParts = URLComponents(string: self)
        
        let templateRoutePathParts = routeTemplateParts!.path.split(separator: "/").map { String($0) }
        let mandatoryArgumentsNames = templateRoutePathParts.dropFirst(
            templateRoutePathParts.count - screen.navArguments.filter({!$0.isOptional}).count
        )
        
        let routePathParts = routeParts!.path.split(separator: "/").map { String($0) }
        let mandatoryArgumentsValues = routePathParts.dropFirst(
            routePathParts.count - screen.navArguments.filter({!$0.isOptional}).count
        )
        
        return Dictionary<String, String>(uniqueKeysWithValues: zip(mandatoryArgumentsNames, mandatoryArgumentsValues))
    }
}

enum IOSScreen: String, CaseIterable {
    case Home
    case Product
    
    var kmmScreen: Screen {
        switch self {
        case .Home:
            return Screen.Home.shared
        case .Product:
            return Screen.Product.shared
        }
    }
}
