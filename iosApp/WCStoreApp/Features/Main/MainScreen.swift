//
//  MainScreen.swift
//  WCStoreApp
//
//  Created by Hicham on 12/9/2022.
//

import SwiftUI
import WCStoreAppKmm
import KMPNativeCoroutinesCombine

class MainViewModelProxy: ViewModelProxy<MainViewModel> {
    @Published var uiState: MainViewModel.UiState!

    override init() {
        super.init()
        uiState = viewModel.uiStateNativeValue
        assignToPublished(from:\.uiStateNative, to: &$uiState)
    }
}

struct MainScreen: View {
    @StateObject private var viewModelProxy = MainViewModelProxy()
    @State private var selectedTab: Int = 0
    
    private var viewModel: MainViewModel {
        return viewModelProxy.viewModel
    }
    
    var body: some View {
        Screen(hasNavigationBar: false) {
            GeometryReader { geo in
                VStack(spacing: 0) {
                    switch (selectedTab) {
                    case 0 : HomeScreen().frame(maxWidth: .infinity, maxHeight: .infinity)
                    case 1 : SearchScreen().frame(maxWidth: .infinity, maxHeight: .infinity)
                    default: EmptyView()
                    }
                    
                    Divider()
                    HStack {
                        TabBarIcon(
                            isSelected: selectedTab == 0,
                            width: geo.size.width / 3,
                            systemIconName: "house",
                            tabName: "Home"
                        ) {
                            selectedTab = 0
                        }
                        CartButton(countOfItemsInCart: viewModelProxy.uiState.countOfItemsInCart, size: geo.size.width / 7) {
                            viewModel.onCartButtonClick()
                        }
                        .offset(y: -geo.size.height/12/2)
                        
                        TabBarIcon(
                            isSelected: selectedTab == 1,
                            width: geo.size.width / 3,
                            systemIconName: "magnifyingglass",
                            tabName: "Search"
                        ) {
                            selectedTab = 1
                        }
                    }
                    .frame(width: geo.size.width, height: geo.size.height/12)
                    .background(Color("TabBarBackground").shadow(radius: 2))
                }
                .frame(
                    maxWidth: .infinity,
                    maxHeight: .infinity,
                    alignment: .topLeading
                )
            }
        }
    }
}

struct CartButton : View {
    let countOfItemsInCart: Int32
    let size: CGFloat
    let onTap: () -> Void
    
    var body: some View {
        ZStack(alignment:.topTrailing) {
            Image(systemName: "cart.circle.fill")
                .resizable()
                .aspectRatio(contentMode: .fit)
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .foregroundColor(Color(.purple))
                .padding(3)
                .background(Color.white)
                .clipShape(Circle())
            if (countOfItemsInCart > 0) {
                Text(countOfItemsInCart.formatted())
                    .font(.system(size: 12))
                    .frame(width: 16, height: 16, alignment: .center)
                    .foregroundColor(Color.white)
                    .background(Color.red)
                    .clipShape(Circle())
            }
        }
        .frame(width: size, height: size)
        .onTapGesture {
            withAnimation {
                onTap()
            }
        }
    }
}

struct TabBarIcon: View {
    
    let isSelected: Bool
    let width: CGFloat
    let systemIconName, tabName: String
    let onTap: () -> Void
    
    var body: some View {
        VStack {
            Image(systemName: systemIconName)
                .resizable()
                .aspectRatio(contentMode: .fit)
                .frame(width: width)
                .padding(.top, 6)
            Text(tabName)
                .font(.footnote)
            Spacer()
        }
        .onTapGesture {
            onTap()
        }
        .foregroundColor(isSelected ? Color.accentColor : .gray)
    }
}
