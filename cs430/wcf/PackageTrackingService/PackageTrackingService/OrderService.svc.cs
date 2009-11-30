using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;

namespace PackageTrackingService
{
	public class OrderService : IOrderService
	{

		#region IOrderService Members

		public string HelloWho(string name)
		{
			return "Hello " + name;
		}

		public int Square(int i)
		{
			return i*i;
		}

		#endregion
	}
}
