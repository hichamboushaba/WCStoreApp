//
//  ProductsList.swift
//  WCStoreApp
//
//  Created by Hicham Boushaba on 10/3/2022.
//

import SwiftUI
import WCStoreAppKmm

struct ProductsList: View {
    @StateObject private var viewModel = ProductsViewModel()
    
    var body: some View {
        List {
            ForEach(viewModel.products, id: \.id) { product in
                ProductsListRowView(product: product)
            }
            if viewModel.shouldDisplayNextPage {
                nextPageView
            }
        }
        .navigationTitle("Products")
    }
    
    private var nextPageView: some View {
        HStack {
            Spacer()
            VStack {
                ProgressView()
                Text("Loading next page...")
            }
            Spacer()
        }
        .onAppear(perform: {
            viewModel.fetchNextData()
        })
    }
}

struct ProductsListRowView: View {
    let product: Product
    
    var body: some View {
        HStack {
            if let image = product.images.first,
               let url = URL(string: image) {
                AsyncImage(url: url) { image in
                    image.resizable()
                } placeholder: {
                    ProgressView()
                }
                .frame(width: 50, height: 50)
                .clipShape(RoundedRectangle(cornerRadius: 8))
            } else {
                RoundedRectangle(cornerRadius: 8)
                    .frame(width: 50, height: 50)
                    .foregroundColor(.gray)
            }
            VStack(alignment: .leading) {
                Text(product.name )
                    .font(.title3)
                    .foregroundColor(.accentColor)
                Text(product.prices.price.toString(base: 10))
                    .font(.footnote)
                    .foregroundColor(.gray)
            }
        }
    }
}


struct ProductsList_Previews: PreviewProvider {
    static var previews: some View {
        ProductsList()
    }
}
